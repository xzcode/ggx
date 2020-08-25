package com.ggx.server.spring.boot.starter.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class GGXServerSpringBootApplicationFailedEventListener implements  ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	
    }
}
