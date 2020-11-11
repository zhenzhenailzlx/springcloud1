package com.zhenzhen.demo.zuul.filter.route;

import com.netflix.client.http.HttpRequest;
import com.netflix.niws.client.http.RestClient;
import com.zhenzhen.demo.zuul.config.CustomZuulProperties;
import lombok.SneakyThrows;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;

/**
 * @author zhen.yin
 * @title:
 * @description:
 * @date 2020/11/9 13:57
 */
public class CustomRestClientRibbonCommandFactory implements RibbonCommandFactory<CustomRestClientRibbonCommand> {
    private final SpringClientFactory clientFactory;

    CustomZuulProperties customZuulProperties;

    public CustomRestClientRibbonCommandFactory(SpringClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public CustomRestClientRibbonCommandFactory(SpringClientFactory clientFactory, CustomZuulProperties customZuulProperties) {
        this.clientFactory = clientFactory;
        this.customZuulProperties = customZuulProperties;
    }

    @Override
    @SuppressWarnings("deprecation")
    @SneakyThrows
    public CustomRestClientRibbonCommand create(RibbonCommandContext context) {
        RestClient restClient = this.clientFactory.getClient(context.getServiceId(),
                RestClient.class);
        return new CustomRestClientRibbonCommand(
                context.getServiceId(), restClient, getVerb(context.getVerb()),
                context.getUri(), context.getRetryable(), context.getHeaders(),
                context.getParams(), context.getRequestEntity(),customZuulProperties);
    }

    protected SpringClientFactory getClientFactory() {
        return this.clientFactory;
    }

    protected static HttpRequest.Verb getVerb(String sMethod) {
        if (sMethod == null)
            return HttpRequest.Verb.GET;
        try {
            return HttpRequest.Verb.valueOf(sMethod.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return HttpRequest.Verb.GET;
        }
    }
}
