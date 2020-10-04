package com.ggx.core.common.future;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 默认future
 *
 * @author zai 2020-04-07 14:26:57
 */
public class GGXDefaultFuture implements GGXFuture {

	private Set<GGXFutureListener<GGXFuture>> listeners = new ConcurrentSkipListSet<>();

	private boolean success;

	private boolean done;

	private boolean cancel;
	
	private Object data;
	
	private GGXSession session;
	
	

	public GGXDefaultFuture() {
	}
	
	

	public GGXDefaultFuture(boolean success, Object data) {
		this.success = success;
		this.data = data;
		this.done = true;
		this.triggerListeners();
	}



	@Override
	public void addListener(GGXFutureListener<GGXFuture> listener) {
		synchronized (this) {
			if (this.done) {
				triggerListener(listener);
			} else {
				listeners.add(listener);
			}
		}

	}

	@Override
	public boolean isSuccess() {
		return this.success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public boolean isDone() {
		return this.done;
	}

	@Override
	public boolean cancel() {
		return false;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		return data;
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return data;
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
	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public void setDone(boolean done) {
		if (this.done) {
			return;
		}
		this.done = done;
		if (this.done && this.listeners.size() > 0) {
			synchronized (this) {
				triggerListeners();
			}
		}
	}

	private void triggerListeners() {
		for (GGXFutureListener<GGXFuture> listener : listeners) {
			triggerListener(listener);
		}
	}

	private void triggerListener(GGXFutureListener<GGXFuture> listener) {
		try {
			listener.operationComplete(this);
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("GGXFuture trigger listener ERROR!", e);
		}
	}

}
