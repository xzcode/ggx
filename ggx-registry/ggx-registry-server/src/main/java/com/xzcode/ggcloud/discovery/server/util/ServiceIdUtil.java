package com.xzcode.ggcloud.discovery.server.util;

import java.util.UUID;

public class ServiceIdUtil {
	
	public static String newServiceId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
