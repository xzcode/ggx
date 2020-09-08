package com.ggx.admin.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.core.spring.support.GGXCoreSpringAnnotationSupport;
import com.ggx.registry.client.RegistryClient;

@Configuration
public class GGXDashboardServerConfiguration implements CommandLineRunner {

	@Autowired
	private RegistryClient registryClient;

	@Bean
	@ConfigurationProperties(prefix = "ggx.admin.collector.server")
	public GGXAdminCollectorServerConfig adminCollectorServerConfig() {
		GGXAdminCollectorServerConfig adminCollectorServerConfig = new GGXAdminCollectorServerConfig();
		return adminCollectorServerConfig;
	}

	@Bean
	public GGXAdminCollectorServer adminCollectorServer() {
		GGXAdminCollectorServer adminCollectorServer = new GGXAdminCollectorServer(adminCollectorServerConfig());
		return adminCollectorServer;
	}

	@Bean(value = "ggserverAdminCollectorServerSpringAnnotationSupport")
	public GGXCoreSpringAnnotationSupport ggxCoreSpringAnnotationSupport() {
		GGXCoreServerConfig serverConfig = adminCollectorServer().getConfig().getServer().getConfig();
		GGXCoreSpringAnnotationSupport support = new GGXCoreSpringAnnotationSupport(
				serverConfig.getReceiveMessageManager(), serverConfig.getEventManager(),
				serverConfig.getFilterManager());
		support.setBasicPackage(new String[] { GGXAdminCollectorServer.class.getPackage().getName() });
		return support;
	}

	@Override
	public void run(String... args) throws Exception {
		adminCollectorServer().start();
	}

}
