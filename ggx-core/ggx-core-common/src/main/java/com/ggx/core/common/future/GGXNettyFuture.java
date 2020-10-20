package com.ggx.core.common.future;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;


/**
 * 基于Netty Future进行外观的实现
 * 
 * @author zai
 * 2019-11-24 17:54:50
 */
public class GGXNettyFuture<T> implements GGXFuture<T> {
	
	private io.netty.util.concurrent.Future<T> nettyFuture;
	
	private Set<GGXFutureListener<T>> listeners;
	
	public GGXNettyFuture() {
		listeners = new LinkedHashSet<>(2);
	}
	
	public GGXNettyFuture(Future<T> nettyFuture) {
		this.setFuture(nettyFuture);
	}

	@SuppressWarnings("unchecked")
	public void setFuture(Future<?> future) {
		if (this.nettyFuture != null) {
			return;
		}
		synchronized (this) {
			this.nettyFuture = (io.netty.util.concurrent.Future<T>) future;		
		}
		if (listeners != null && listeners.size() > 0) {
			for (GGXFutureListener<T> listener : listeners) {
				nettyFuture.addListener((f) -> {
					listener.operationComplete(this);
				});
			}
		}
	}

	@Override
	public void addListener(GGXFutureListener<T> listener) {
		try {
				synchronized (this) {
					if (nettyFuture == null) {
						listeners.add(listener);
						return;
					}
					nettyFuture.addListener((f) -> {
						listener.operationComplete(this);
					});
				}
		} catch (Exception e) {
			GGXLogUtil.getLogger().error("IGGFuture 'operationComplete' Error!", e);
		}
		
	}

	@Override
	public boolean isSuccess() {
		return this.nettyFuture.isSuccess();
	}

	@Override
	public boolean isDone() {
		return this.nettyFuture.isDone();
	}

	@Override
	public boolean cancel() {
		if (this.nettyFuture != null) {
			return this.nettyFuture.cancel(false);
		}
		return true;
	}
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (this.nettyFuture != null) {
			return this.nettyFuture.cancel(mayInterruptIfRunning);
		}
		return true;
	}

	@Override
	public boolean isCancelled() {
		return this.nettyFuture.isCancelled();
	}

	@Override
	public T get(){
		try {
			return this.nettyFuture.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit){
		try {
			return this.nettyFuture.get(timeout, unit);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GGXSession getSession() {
		if (this.nettyFuture instanceof ChannelFuture) {
			Channel channel = ((ChannelFuture)this.nettyFuture).channel();
			if (channel != null) {
				return (GGXSession) channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
			}
		}
		return null;
	}

	@Override
	public Throwable cause() {
		return this.nettyFuture.cause();
	}

}
