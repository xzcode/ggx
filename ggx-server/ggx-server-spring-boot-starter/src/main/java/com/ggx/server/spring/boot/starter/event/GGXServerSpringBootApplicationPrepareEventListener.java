package com.ggx.server.spring.boot.starter.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.ggx.server.starter.GGXServer;

public class GGXServerSpringBootApplicationPrepareEventListener implements  ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	@Autowired
	protected GGXServer ggxServer;
	
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    	//ggxServer.start();
    }
}
