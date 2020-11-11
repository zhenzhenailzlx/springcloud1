package com.zhenzhen.demo.eureka.listener;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaInstanceRegisteredEventListener {

    @EventListener
    public void register(EurekaInstanceRegisteredEvent e){
        System.out.println("**************register " + e.getInstanceInfo().getAppName() + ", vip " + e.getInstanceInfo().getVIPAddress()
				+ ", leaseDuration " + e.getLeaseDuration());
    }

}
