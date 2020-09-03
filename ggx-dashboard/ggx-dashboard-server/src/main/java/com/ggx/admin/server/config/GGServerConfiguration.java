package com.ggx.admin.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.server.GGXCoreServer;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.server.impl.GGXDefaultCoreServer;
import com.ggx.core.spring.support.GGXCoreSpringAnnotationSupport;
import com.ggx.registry.client.RegistryClient;

@Configuration
public class GGServerConfiguration implements CommandLineRunner {

	@Autowired
	private RegistryClient registryClient;
	
	@Bean
	@ConfigurationProperties(prefix = "ggx.core")
	public GGXCoreServerConfig ggServerConfig() {
		GGXCoreServerConfig serverConfig = new GGXCoreServerConfig();
		serverConfig.setEnableAesEncryption(true);
		serverConfig.setPingPongEnabled(true);
		serverConfig.init();
		return serverConfig;
	}

	@Bean
	public GGXCoreServer ggserver() {
		GGXDefaultCoreServer ggserver = new GGXDefaultCoreServer(ggServerConfig());
		return ggserver;
	}
	
	@Bean(value = "ggserverSpringAnnotationSupport")
	public GGXCoreSpringAnnotationSupport ggxCoreSpringAnnotationSupport() {
		 GGXCoreServerConfig serverConfig = ggServerConfig();
		 GGXCoreSpringAnnotationSupport support = new GGXCoreSpringAnnotationSupport(serverConfig.getReceiveMessageManager(), serverConfig.getEventManager(), serverConfig.getFilterManager());
		 support.setBasicPackage(new String[]{"com.ggx.admin.server"});
		 return support;
	}

	@Override
	public void run(String... args) throws Exception {
		ggserver().start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}
		});
	}

}
