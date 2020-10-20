package com.ggx.core.common.future;


import java.util.concurrent.TimeUnit;

import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;


/**
 * 默认失败future
 * @param 
 * 
 * @author zai
 * 2019-12-01 16:28:44
 */
public class GGXFailedFuture<T> implements GGXFuture<T> {
	
	public static final GGXFuture<?> DEFAULT_FAILED_FUTURE = new GGXFailedFuture<>();
	
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
		return false;
	}

	@Override
	public T get()  {
		return null;
	}

	@Override
	public T get(long timeout, TimeUnit unit) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addListener(GGXFutureListener<T> listener) {
		try {
			listener.operationComplete((GGXFuture<T>) DEFAULT_FAILED_FUTURE);
		} catch (Exception e) {
			GGXLogUtil.getLogger().error("IGGFuture 'operationComplete' Error!", e);
		}
		
	}
	


	@Override
	public boolean cancel() {
		return false;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public GGXSession getSession() {
		return null;
	}

	@Override
	public Throwable cause() {
		return null;
	}


}
