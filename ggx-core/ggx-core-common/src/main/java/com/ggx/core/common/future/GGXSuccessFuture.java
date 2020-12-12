package com.ggx.core.common.future;

import java.util.concurrent.TimeUnit;

import com.ggx.core.common.session.GGXSession;
import com.ggx.util.future.Future;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 默认成功future
 * 
 * @param <V>
 * 
 * @author zai 2019-12-01 16:28:44
 */
public class GGXSuccessFuture<T> implements GGXFuture<T> {

	public static final GGXSuccessFuture<?> DEFAULT_SUCCESS_FUTURE = new GGXSuccessFuture<>();

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
	public T get() {
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
			listener.operationComplete((Future<T>) DEFAULT_SUCCESS_FUTURE);
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
		return true;
	}

	@Override
	public GGXSession getSession() {
		return null;
	}

	@Override
	public Throwable cause() {
		return null;
	}

	@Override
	public T get(long timeout) {
		return null;
	}

}
