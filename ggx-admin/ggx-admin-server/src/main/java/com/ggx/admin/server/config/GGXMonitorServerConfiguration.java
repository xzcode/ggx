package com.ggx.admin.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.monitor.server.GameMonitorServer;
import com.ggx.monitor.server.config.GameMonitorServerConfig;

@Configuration
public class GGXMonitorServerConfiguration{
	
	@Bean
	public GameMonitorServer gameMonitorServer() {
		GameMonitorServerConfig gameMonitorServerConfig = gameMonitorServerConfig();
		GameMonitorServer gameMonitorServer = new GameMonitorServer(gameMonitorServerConfig);
		return gameMonitorServer;
	}
	
	@ConfigurationProperties("ggx.monitor.server")
	@Bean
	public GameMonitorServerConfig gameMonitorServerConfig() {
		GameMonitorServerConfig gameMonitorServerConfig = new GameMonitorServerConfig();
		return gameMonitorServerConfig;
	}

}
