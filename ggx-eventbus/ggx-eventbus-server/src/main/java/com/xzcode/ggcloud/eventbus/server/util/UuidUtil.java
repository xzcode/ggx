package com.xzcode.ggcloud.eventbus.server.util;

import java.util.UUID;

public class UuidUtil {
	
	public static String newServiceId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
