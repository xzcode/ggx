package com.ggx.admin.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.core.common.handler.serializer.impl.JsonSerializer;
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

	@Override
	public void run(String... args) throws Exception {
		ggserver().start().addListener(f -> {
			if (f.isSuccess()) {
				this.registryClient.start();
			}
		});
	}

}
