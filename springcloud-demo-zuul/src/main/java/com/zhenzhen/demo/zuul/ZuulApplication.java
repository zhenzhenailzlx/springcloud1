package com.zhenzhen.demo.zuul;

import com.netflix.discovery.converters.Auto;
import com.zhenzhen.demo.zuul.config.CustomZuulProperties;
import com.zhenzhen.demo.zuul.filter.route.CustomRestClientRibbonCommandFactory;
import com.zhenzhen.demo.zuul.filter.route.CustomRibbonRoutingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringCloudApplication
public class ZuulApplication {

	@Bean
	public PatternServiceRouteMapper serviceRouteMapper() {
		return new PatternServiceRouteMapper("(?<name>^.+)", "api/${name}");
	}

	@Autowired
	private CustomZuulProperties customZuulProperties;


	@Bean
	public CustomRibbonRoutingFilter accessFilter(ProxyRequestHelper helper,
												  SpringClientFactory clientFactory) {
		return new CustomRibbonRoutingFilter(helper, new CustomRestClientRibbonCommandFactory(clientFactory,customZuulProperties));
	}


	public static void main(String[] args) {
	    new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
	}
	


}
