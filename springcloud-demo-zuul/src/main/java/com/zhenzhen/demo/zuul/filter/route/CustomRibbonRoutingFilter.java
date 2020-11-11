package com.zhenzhen.demo.zuul.filter.route;

import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author zhen.yin
 * @title:
 * @description:
 * 重写RibbonRoutingFilter
 * 1.默认使用信号量隔离，修改使用线程池隔离
 * 2.默认不支持单独下游服务配置信号量隔离，扩展支持单独线程池隔离
 * 3.默认重试需要条件，修改成不需要条件
 * 4.自定义记录错误日志
 * @date 2020/11/9 13:46
 */
@CommonsLog
public class CustomRibbonRoutingFilter extends ZuulFilter {


    protected ProxyRequestHelper helper;
    protected RibbonCommandFactory<?> ribbonCommandFactory;

    public CustomRibbonRoutingFilter(ProxyRequestHelper helper,
                                     RibbonCommandFactory<?> ribbonCommandFactory) {
        this.helper = helper;
        this.ribbonCommandFactory = ribbonCommandFactory;
    }

    public CustomRibbonRoutingFilter(RibbonCommandFactory<?> ribbonCommandFactory) {
        this(new ProxyRequestHelper(), ribbonCommandFactory);
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (ctx.getRouteHost() == null && ctx.get("serviceId") != null
                && ctx.sendZuulResponse());
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        this.helper.addIgnoredHeaders();
        try {
            RibbonCommandContext commandContext = buildCommandContext(context);
            ClientHttpResponse response = forward(commandContext);
            setResponse(response);
            return response;
        }
        catch (ZuulException ex) {
            context.set("error.status_code", ex.nStatusCode);
            context.set("error.message", ex.errorCause);
            context.set("error.exception", ex);
        }
        catch (Exception ex) {
            context.set("error.status_code",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.set("error.exception", ex);
        }
        return null;
    }

    protected RibbonCommandContext buildCommandContext(RequestContext context) {
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = this.helper
                .buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper
                .buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        String serviceId = (String) context.get("serviceId");
        //Boolean retryable = (Boolean) context.get("retryable");
        Boolean retryable = Boolean.TRUE;

        String uri = this.helper.buildZuulRequestURI(request);

        // remove double slashes
        uri = uri.replace("//", "/");

        return new RibbonCommandContext(serviceId, verb, uri, retryable, headers, params,
                requestEntity);
    }

    protected ClientHttpResponse forward(RibbonCommandContext context) throws Exception {
        Map<String, Object> info = this.helper.debug(context.getVerb(), context.getUri(),
                context.getHeaders(), context.getParams(), context.getRequestEntity());

        RibbonCommand command = this.ribbonCommandFactory.create(context);
        try {
            ClientHttpResponse response = command.execute();
            this.helper.appendDebug(info, response.getStatusCode().value(),
                    response.getHeaders());
            return response;
        }
        catch (HystrixRuntimeException ex) {
            return handleException(info, ex);
        }

    }

    protected ClientHttpResponse handleException(Map<String, Object> info,
                                                 HystrixRuntimeException ex) throws ZuulException {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        Throwable cause = ex;
        String message = ex.getFailureType().toString();

        ClientException clientException = findClientException(ex);
        if (clientException == null) {
            clientException = findClientException(ex.getFallbackException());
        }

        if (clientException != null) {
            if (clientException
                    .getErrorType() == ClientException.ErrorType.SERVER_THROTTLED) {
                statusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
            }
            cause = clientException;
            message = clientException.getErrorType().toString();
        }
        info.put("status", String.valueOf(statusCode));
        throw new ZuulException(cause, "Forwarding error", statusCode, message);
    }

    protected ClientException findClientException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof ClientException) {
            return (ClientException) t;
        }
        return findClientException(t.getCause());
    }

    protected InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        // ApacheHttpClient4Handler does not support body in delete requests
        if (request.getMethod().equals("DELETE")) {
            return null;
        }
        try {
            requestEntity = (InputStream) RequestContext.getCurrentContext()
                    .get("requestEntity");
            if (requestEntity == null) {
                requestEntity = request.getInputStream();
            }
        }
        catch (IOException ex) {
            log.error("Error during getRequestBody", ex);
        }
        return requestEntity;
    }

    protected String getVerb(HttpServletRequest request) {
        String method = request.getMethod();
        if (method == null) {
            return "GET";
        }
        return method;
    }

    protected void setResponse(ClientHttpResponse resp)
            throws ClientException, IOException {
        this.helper.setResponse(resp.getStatusCode().value(),
                resp.getBody() == null ? null : resp.getBody(), resp.getHeaders());
    }

}
