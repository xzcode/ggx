package com.ggx.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.eventbus.group.client.EventbusGroupClient;
import com.ggx.eventbus.group.client.config.EventbusGroupClientConfig;
import com.ggx.registry.client.RegistryClient;

@Configuration
public class GGXEventbusGroupClientConfiguration {
	
	@Autowired
	private RegistryClient registryClient;
	
	@Bean
	public EventbusGroupClient eventbusGroupClient() {
		EventbusGroupClientConfig eventbusGroupClientConfig = eventbusGroupClientConfig();
		EventbusGroupClient eventbusGroupClient = new EventbusGroupClient(eventbusGroupClientConfig);
		/*
		 * eventbusGroupClient.subscribe("enter.req.event", new Subscriber<TestData>() {
		 * 
		 * @Override public void trigger(TestData data) {
		 * System.out.println("enter.req.event:"+ new Gson().toJson(data));
		 * 
		 * } });
		 */
		return eventbusGroupClient;
	}
	
	@ConfigurationProperties("ggx.eventbus.group.client")
	@Bean
	public EventbusGroupClientConfig eventbusGroupClientConfig() {
		EventbusGroupClientConfig eventbusGroupClientConfig = new EventbusGroupClientConfig();
		eventbusGroupClientConfig.setRegistryClient(registryClient);
		return eventbusGroupClientConfig;
	}

}
