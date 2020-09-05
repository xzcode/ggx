package com.ggx.server.spring.boot.starter.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootApplicationReadyEventListener implements  ApplicationListener<ApplicationFailedEvent> {

	@Autowired
	protected GGXServer ggxServer;
	
    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
    	ggxServer.shutdown();
    }
}
