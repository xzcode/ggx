package com.ggx.core.common.future;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 默认future
 *
 * @author zai 2020-04-07 14:26:57
 */
public class GGXCoreFuture<T> implements GGXFuture<T> {

	private Throwable cause;

	private boolean success;

	private boolean done;

	private boolean cancel;
	
	private boolean waiting;
	
	private long defaultWaitTimeout = 30L * 1000L;//默认超时时间 ms
	
	private Object data;
	
	private Class<?> dataType;
	
	private GGXSession session;
	
	
	private List<GGXFutureListener<T>> listeners = new CopyOnWriteArrayList<>();

	public GGXCoreFuture() {
	}
	
	public GGXCoreFuture(boolean success) {
		this.success = success;
		this.done = true;
		this.triggerListeners();
	}

	public GGXCoreFuture(boolean success, T data) {
		this.success = success;
		this.data = data;
		this.done = true;
		this.triggerListeners();
	}
	
	public GGXCoreFuture(boolean success, Throwable cause) {
		this.success = success;
		this.cause = cause;
		this.done = true;
		this.triggerListeners();
	}

	@Override
	public GGXFuture<T> addListener(GGXFutureListener<T> listener) {
		synchronized (this) {
			if (this.done) {
				triggerListener(listener);
			} else {
				listeners.add(listener);
			}
		}
		return this;
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
		/*
		 * if (this.cause() != null) { if (this.cause instanceof
		 * GGXNoStackTraceRuntimeException) {
		 * GGXLogUtil.getLogger(this).error("GGXFuture.get() Error!", cause); } }
		 */		
		return (T) this.data;
	}

	@Override
	public T get() {
		return this.get(0);
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
	
	public void setDone(boolean success, Object data, Throwable cause, boolean done) {
		this.setSuccess(success);
		this.setData(data);
		this.setCause(cause);
		this.setDone(done);
	}
	
	public void setDone(boolean success, Object data, Throwable cause) {
		this.setSuccess(success);
		this.setData(data);
		this.setCause(cause);
		this.setDone(true);
	}
	public void doneWithError(Throwable cause) {
		this.setCause(cause);
		this.setDone(true);
	}
	public void doneWithSuccess(Object data) {
		this.setData(data);
		this.setDone(true);
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
		for (GGXFutureListener<T> listener : listeners) {
			triggerListener(listener);
		}
	}

	private void triggerListener(GGXFutureListener<T> listener) {
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
	
	/**
	 * 跟随指定future
	 *
	 * @param followFuture
	 * 2021-03-03 12:08:36
	 */
	public void follow(GGXFuture<?> followFuture) {
		followFuture.addListener(f -> {
			this.setDone(followFuture.isSuccess(), followFuture.get(), followFuture.cause(), followFuture.isDone());
		});
	}
	
	/**
	 * 跟随指定future
	 *
	 * @param executor
	 * @param followFuture
	 * 2021-03-03 12:08:50
	 */
	public void follow(TaskExecutor executor, GGXFuture<?> followFuture) {
		followFuture.addListener(executor, f -> {
			this.setDone(followFuture.isSuccess(), followFuture.get(), followFuture.cause(), followFuture.isDone());
		});
	}
	
}
