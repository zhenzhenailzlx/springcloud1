package com.zhenzhen.demo.zuul.filter.route;

import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.*;
import com.netflix.niws.client.http.RestClient;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.zhenzhen.demo.zuul.config.CustomZuulProperties;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author zhen.yin
 * @title:
 * @description:
 * @date 2020/11/9 14:00
 */
public class CustomRestClientRibbonCommand extends HystrixCommand<ClientHttpResponse> implements RibbonCommand {

    private RestClient restClient;

    private HttpRequest.Verb verb;

    private URI uri;

    private Boolean retryable;

    private MultiValueMap<String, String> headers;

    private MultiValueMap<String, String> params;

    private InputStream requestEntity;

    public CustomRestClientRibbonCommand(RestClient restClient, HttpRequest.Verb verb, String uri,
                                   Boolean retryable,
                                   MultiValueMap<String, String> headers,
                                   MultiValueMap<String, String> params, InputStream requestEntity)
            throws URISyntaxException {
        this("default", restClient, verb, uri, retryable , headers, params, requestEntity);
    }

    public CustomRestClientRibbonCommand(String commandKey, RestClient restClient, HttpRequest.Verb verb, String uri,
                                   Boolean retryable,
                                   MultiValueMap<String, String> headers,
                                   MultiValueMap<String, String> params, InputStream requestEntity)
            throws URISyntaxException {
        super(getSetter(commandKey));
        this.restClient = restClient;
        this.verb = verb;
        this.uri = new URI(uri);
        this.retryable = retryable;
        this.headers = headers;
        this.params = params;
        this.requestEntity = requestEntity;
    }

    public CustomRestClientRibbonCommand(String commandKey, RestClient restClient, HttpRequest.Verb verb, String uri, Boolean retryable, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, InputStream requestEntity, CustomZuulProperties customZuulProperties)   throws URISyntaxException {
        super(getSetter(commandKey,customZuulProperties));
        this.restClient = restClient;
        this.verb = verb;
        this.uri = new URI(uri);
        this.retryable = retryable;
        this.headers = headers;
        this.params = params;
        this.requestEntity = requestEntity;
    }

    protected static HystrixCommand.Setter getSetter(String commandKey, CustomZuulProperties customZuulProperties) {

        // @formatter:off
        Setter commandSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RibbonCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));

        final HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(customZuulProperties.getRibbonIsolationStrategy());

        if (customZuulProperties.getRibbonIsolationStrategy() == HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE){
            final String name = ZuulConstants.ZUUL_EUREKA + commandKey + ".semaphore.maxSemaphores";
            // we want to default to semaphore-isolation since this wraps
            // 2 others commands that are already thread isolated
            final DynamicIntProperty value = DynamicPropertyFactory.getInstance()
                    .getIntProperty(name, customZuulProperties.getSemaphore().getMaxSemaphores());
            setter.withExecutionIsolationSemaphoreMaxConcurrentRequests(value.get());
        } else if (customZuulProperties.getThreadPool().isUseSeparateThreadPools()) {
            final String threadPoolKey = customZuulProperties.getThreadPool().getThreadPoolKeyPrefix() + commandKey;
            commandSetter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey));
        } else if(!StringUtils.isEmpty(customZuulProperties.getSeparateThreadPoolKeys())) {
            //当SeparateThreadPoolKeys不为空
            if(customZuulProperties.getSeparateThreadPoolKeys().indexOf(commandKey) != -1) {
                //如果包含commandKey
                final String threadPoolKey = customZuulProperties.getThreadPool().getThreadPoolKeyPrefix() + commandKey;
                commandSetter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey));
            }
        }

        return commandSetter.andCommandPropertiesDefaults(setter);
        // @formatter:on
    }

    protected static HystrixCommand.Setter getSetter(String commandKey) {
        // we want to default to semaphore-isolation since this wraps
        // 2 others commands that are already thread isolated
        String name = ZuulConstants.ZUUL_EUREKA + commandKey + ".semaphore.maxSemaphores";
        DynamicIntProperty value = DynamicPropertyFactory.getInstance().getIntProperty(
                name, 100);
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(value.get());
        return Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RibbonCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andCommandPropertiesDefaults(setter);
    }

    @Override
    protected ClientHttpResponse run() throws Exception {
        return forward();
    }

    protected ClientHttpResponse forward() throws Exception {
        RequestContext context = RequestContext.getCurrentContext();
        HttpRequest.Builder builder = HttpRequest.newBuilder().verb(this.verb).uri(this.uri)
                .entity(this.requestEntity);

        if(this.retryable != null) {
            builder.setRetriable(this.retryable);
        }

        for (String name : this.headers.keySet()) {
            List<String> values = this.headers.get(name);
            for (String value : values) {
                builder.header(name, value);
            }
        }
        for (String name : this.params.keySet()) {
            List<String> values = this.params.get(name);
            for (String value : values) {
                builder.queryParams(name, value);
            }
        }

        customizeRequest(builder);

        HttpRequest httpClientRequest = builder.build();
        HttpResponse response = this.restClient
                .executeWithLoadBalancer(httpClientRequest);
        context.set("ribbonResponse", response);

        // Explicitly close the HttpResponse if the Hystrix command timed out to
        // release the underlying HTTP connection held by the response.
        //
        if( this.isResponseTimedOut() ) {
            if( response!= null ) {
                response.close();
            }
        }

        RibbonHttpResponse ribbonHttpResponse = new RibbonHttpResponse(response);

        return ribbonHttpResponse;
    }

    protected void customizeRequest(HttpRequest.Builder requestBuilder) {

    }

    protected MultiValueMap<String, String> getHeaders() {
        return this.headers;
    }

    protected MultiValueMap<String, String> getParams() {
        return this.params;
    }

    protected InputStream getRequestEntity() {
        return this.requestEntity;
    }

    protected RestClient getRestClient() {
        return this.restClient;
    }

    protected Boolean getRetryable() {
        return this.retryable;
    }

    protected URI getUri() {
        return this.uri;
    }

    protected HttpRequest.Verb getVerb() {
        return this.verb;
    }

}
