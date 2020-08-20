package com.ggx.admin.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.client.config.RegistryClientConfig;

@Configuration
public class GGXRegistryClientConfiguration{
	
	@Bean
	public RegistryClient registryClient() {
		RegistryClientConfig registryClientConfig = registryClientConfig();
		RegistryClient registryClient = new RegistryClient(registryClientConfig);
		return registryClient;
	}
	
	@ConfigurationProperties("ggx.registry.client")
	@Bean
	public RegistryClientConfig registryClientConfig() {
		RegistryClientConfig registryClientConfig = new RegistryClientConfig();
		return registryClientConfig;
	}

}
