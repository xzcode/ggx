package com.ggx.util.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GGXLoggerUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(GGXLoggerUtil.class);
	
	
	public static Logger getLogger() {
		return LOGGER;
		 
	}
	public static Logger getLogger(Object object) {
		return LoggerFactory.getLogger(object.getClass());
		
	}
	
	
}
