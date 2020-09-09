package com.ggx.dashboard.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务器启动类
 * 
 * 
 * @author zai
 * 2018-06-16 19:57:43
 */
@SpringBootApplication
public class GGXDashboardServerApplication{
	
	private static Logger logger = LoggerFactory.getLogger(GGXDashboardServerApplication.class);
	
	public static void main(String[] args) { 
		SpringApplication.run(GGXDashboardServerApplication.class, args);
	}
	
}

