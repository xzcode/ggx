package com.ggx.admin.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.spring.support.GGXCoreSpringAnnotationSupport;
import com.ggx.registry.client.RegistryClient;
import com.xzcode.ggserver.core.server.GGServer;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.impl.GGDefaultServer;

@Configuration
public class GGServerConfiguration implements CommandLineRunner {

	@Autowired
	private RegistryClient registryClient;
	
	@Bean
	@ConfigurationProperties(prefix = "ggx.core")
	public GGServerConfig ggServerConfig() {
		GGServerConfig serverConfig = new GGServerConfig();
		serverConfig.setEnableAesEncryption(true);
		serverConfig.setPingPongEnabled(true);
		serverConfig.init();
		return serverConfig;
	}

	@Bean
	public GGServer ggserver() {
		GGDefaultServer ggserver = new GGDefaultServer(ggServerConfig());
		return ggserver;
	}
	
	@Bean(value = "ggserverSpringAnnotationSupport")
	public GGXCoreSpringAnnotationSupport ggxCoreSpringAnnotationSupport() {
		 GGServerConfig serverConfig = ggServerConfig();
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
