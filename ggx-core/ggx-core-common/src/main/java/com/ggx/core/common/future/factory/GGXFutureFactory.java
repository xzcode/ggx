package com.ggx.core.common.future.factory;

import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXSuccessFuture;

/**
 * GGXFuture工厂
 * 
 * @author zai 2020-10-6 15:37:35
 */
public class GGXFutureFactory {

	public static final GGXFailedFuture<?> DEFAULT_FAILED_FUTURE = new GGXFailedFuture<>();

	public static final GGXSuccessFuture<?> DEFAULT_SUCCESS_FUTURE = new GGXSuccessFuture<>();

	public static GGXCoreFuture<?> success(Object data) {
		return new GGXCoreFuture<>(true, data);
	}

	public static GGXCoreFuture<?> fail(Throwable cause) {
		return new GGXCoreFuture<>(false, cause);
	}

	public static GGXCoreFuture<?> create() {
		return new GGXCoreFuture<>();
	}

}
