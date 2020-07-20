package com.ggx.admin.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ggx.admin.collector.server.GGXAdminCollectorServer;
import com.ggx.admin.collector.server.config.GGXAdminCollectorServerConfig;
import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerEvents;
import com.ggx.admin.server.event.CollectorServerAuthSuccessEventHandler;
import com.ggx.admin.server.event.CollectorServerConnCloseEventHandler;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.spring.support.GGXCoreSpringAnnotationSupport;
import com.ggx.registry.client.RegistryClient;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

@Configuration
public class GGAdminCollectorServerConfiguration implements CommandLineRunner {

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
		//添加信息收集服务端连接断开监听器
		adminCollectorServer.addEventListener(GGEvents.Connection.CLOSED, new CollectorServerConnCloseEventHandler());
		//添加信息收集服务端注册成功监听器
		adminCollectorServer.addEventListener(GGXAdminCollectorServerEvents.AUTH_SUCCESS, new CollectorServerAuthSuccessEventHandler());
		return adminCollectorServer;
	}
	
	@Bean(value = "ggserverAdminCollectorServerSpringAnnotationSupport")
	public GGXCoreSpringAnnotationSupport ggxCoreSpringAnnotationSupport() {
		 GGServerConfig serverConfig = adminCollectorServer().getConfig().getServer().getConfig();
		 GGXCoreSpringAnnotationSupport support = new GGXCoreSpringAnnotationSupport(serverConfig.getReceiveMessageManager(), serverConfig.getEventManager(), serverConfig.getFilterManager());
		 support.setBasicPackage(new String[]{GGXAdminCollectorServer.class.getPackage().getName()});
		 return support;
	}

	@Override
	public void run(String... args) throws Exception {
		adminCollectorServer().start();
	}

}
