package com.ggx.core.common.future;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLoggerUtil;


/**
 * 默认成功future
 * @param <V>
 * 
 * @author zai
 * 2019-12-01 16:28:44
 */
public class GGSuccessFuture<V> implements GGXFuture {
	
	public static final GGSuccessFuture<?> DEFAULT_SUCCESS_FUTURE = new GGSuccessFuture<>();
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return null;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

	@Override
	public void addListener(GGXFutureListener<GGXFuture> listener) {
		try {
			listener.operationComplete(DEFAULT_SUCCESS_FUTURE);
		} catch (Exception e) {
			GGXLoggerUtil.getLogger().error("IGGFuture 'operationComplete' Error!", e);
		}
	}
	

	@Override
	public boolean cancel() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		try {
			return (T) get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public GGXSession getSession() {
		return null;
	}

}
