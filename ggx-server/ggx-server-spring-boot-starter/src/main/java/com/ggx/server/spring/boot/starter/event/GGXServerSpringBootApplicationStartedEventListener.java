package com.ggx.server.spring.boot.starter.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootApplicationStartedEventListener implements  ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	protected GGXServer ggxServer;
	
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
    	ggxServer.start();
    }
}
