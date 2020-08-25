package com.ggx.server.spring.boot.starter.event;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

public class GGXServerSpringBootApplicationReadyEventListener implements  ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
    	
    }
}
