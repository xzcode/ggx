package com.ggx.core.common.future.factory;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXSuccessFuture;

/**
 * GGXFuture工厂
 * 
 * @author zai
 * 2020-10-6 15:37:35
 */
public class GGXFutureFactory {
	
	public static final GGXFailedFuture DEFAULT_FAILED_FUTURE = new GGXFailedFuture();
	
	public static final GGXSuccessFuture DEFAULT_SUCCESS_FUTURE = new GGXSuccessFuture();
	
	public static GGXDefaultFuture success(Object data) {
		return new GGXDefaultFuture(true, data);
	}
	
	public static GGXDefaultFuture fail(Throwable cause) {
		return new GGXDefaultFuture(false, cause);
	}
	
	public static GGXDefaultFuture create() {
		return new GGXDefaultFuture();
	}
	

}
