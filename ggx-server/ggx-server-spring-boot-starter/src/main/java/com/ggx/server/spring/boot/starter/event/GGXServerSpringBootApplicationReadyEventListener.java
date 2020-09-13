package com.ggx.server.spring.boot.starter.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootApplicationReadyEventListener implements  ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	protected GGXServer ggxServer;
	
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	ggxServer.start();
    }
}
