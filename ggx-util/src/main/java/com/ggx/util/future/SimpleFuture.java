package com.ggx.util.future;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.ggx.util.logger.GGXLogUtil;

public class SimpleFuture<T> implements Future<T> {

	private Throwable cause;

	private boolean success;

	private boolean done;

	private boolean cancel;
	
	private boolean waiting;
	
	private long defaultWaitTimeout = 30L * 1000L;//默认超时时间 ms
	
	private Object data;
	
	private Class<?> dataType;
	
	
	
	private List<FutureListener<T>> listeners = new CopyOnWriteArrayList<>();

	public SimpleFuture() {
	}
	
	
	
	public SimpleFuture(boolean success) {
		this.success = success;
		this.done = true;
		this.triggerListeners();
	}

	public SimpleFuture(boolean success, T data) {
		this.success = success;
		this.data = data;
		this.done = true;
		this.triggerListeners();
	}
	
	public SimpleFuture(boolean success, Throwable cause) {
		this.success = success;
		this.cause = cause;
		this.done = true;
		this.triggerListeners();
	}
	
	@Override
	public void addListener(FutureListener<T> listener) {
		synchronized (this) {
			if (this.done) {	
				triggerListener((FutureListener<T>) listener);
			} else {
				listeners.add((FutureListener<T>) listener);
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
	public T get(long timeout, TimeUnit unit) {
		return this.get(unit.toMillis(timeout));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(long timeout) {
		try {
			synchronized (this) {
				if (!this.done) {
					this.waiting = true;
					if (timeout <= 0L) {
						this.wait(this.defaultWaitTimeout);
					}else {
						this.wait(timeout);
					}
				}
			}
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("GGXFuture.get() Error!", e);
		}
		if (this.cause() != null) {
			throw new RuntimeException(this.cause());
		}
		return (T) this.data;
	}

	@Override
	public T get() {
		return this.get(0);
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
		for (FutureListener<T> listener : listeners) {
			triggerListener(listener);
		}
	}

	private void triggerListener(FutureListener<T> listener) {
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



	public Class<?> getDataType() {
		return dataType;
	}
	
	public void setDefaultWaitTimeout(long waitTimeout) {
		if (waitTimeout > 0L) {
			this.defaultWaitTimeout = waitTimeout;
		}
	}



	
}
