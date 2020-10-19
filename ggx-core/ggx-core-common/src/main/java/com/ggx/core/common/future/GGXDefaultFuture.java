package com.ggx.core.common.future;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

	private Throwable cause;

	private boolean success;

	private boolean done;

	private boolean cancel;
	
	private boolean waiting;
	
	private Object data;
	
	private Class<?> dataType;
	
	private GGXSession session;
	
	
	private List<GGXFutureListener<GGXFuture>> listeners = new CopyOnWriteArrayList<>();

	public GGXDefaultFuture() {
	}
	
	
	
	public GGXDefaultFuture(boolean success) {
		this.success = success;
		this.done = true;
		this.triggerListeners();
	}

	public GGXDefaultFuture(boolean success, Object data) {
		this.success = success;
		this.data = data;
		this.done = true;
		this.triggerListeners();
	}
	
	public GGXDefaultFuture(boolean success, Throwable cause) {
		this.success = success;
		this.cause = cause;
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
		if (this.data != null) {
			this.dataType = data.getClass();
		}
	}
	
	public void setDone(boolean done) {
		if (this.done) {
			return;
		}
		synchronized (this) {
			if (this.waiting) {
				this.notify();
			}
			this.done = done;
			if (this.done && this.listeners.size() > 0) {
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
	
	public Throwable cause() {
		return cause;
	}
	
	public void setCause(Throwable cause) {
		this.cause = cause;
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSync(Class<T> clazz) {
		try {
			synchronized (this) {
				if (this.data == null) {
					this.waiting = true;
					this.wait();
				}
			}
			if (this.cause() != null) {
				throw this.cause();
			}
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("GGXFuture.getSync Error!");
		}
		return (T) this.data;
	}

	public Class<?> getDataType() {
		return dataType;
	}
}
