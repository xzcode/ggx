package com.ggx.util.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GGXLogUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(GGXLogUtil.class);
	
	
	public static Logger getLogger() {
		return LOGGER;
		 
	}
	public static Logger getLogger(Object object) {
		return LoggerFactory.getLogger(object.getClass());
		
	}
	
	public static boolean isTraceEnabled() {
		return LOGGER.isTraceEnabled();
	}
	public static boolean isDebugEnabled() {
		return LOGGER.isDebugEnabled();
	}
	public static boolean isInfoEnabled() {
		return LOGGER.isInfoEnabled();
	}
	public static boolean isWarnEnabled() {
		return LOGGER.isWarnEnabled();
	}
	public static boolean isErrorEnabled() {
		return LOGGER.isErrorEnabled();
	}
	
	
}
