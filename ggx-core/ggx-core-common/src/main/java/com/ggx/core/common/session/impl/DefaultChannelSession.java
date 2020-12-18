package com.ggx.core.common.session.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXNettyFuture;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * sesson默认实现
 * 
 * 
 * @author zai 2019-10-02 22:48:34
 */
public class DefaultChannelSession extends AbstractSession<GGXCoreConfig> {

	private Channel channel;

	public DefaultChannelSession(String sessionId, Channel channel, GGXCoreConfig config) {
		super(sessionId, new SingleThreadTaskExecutor(channel.eventLoop()), config);
		this.channel = channel;

	}

	@Override
	public void addAttribute(String key, Object value) {
		channel.attr(AttributeKey.valueOf(key)).set(value);
	}

	@Override
	public Object getAttribute(String key) {
		return channel.attr(AttributeKey.valueOf(key)).get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String key, Class<T> t) {
		return (T) channel.attr(AttributeKey.valueOf(key)).get();
	}

	@Override
	public Object reomveAttribute(String key) {
		return channel.attr(AttributeKey.valueOf(key)).getAndSet(null);
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public boolean isReady() {
		return this.ready;
	}

	@Override
	public String getGroupId() {
		return null;
	}

	@Override
	public GGXFuture<?> disconnect() {
		GGXNettyFuture<?> future = new GGXNettyFuture<>(this.getChannel().close());
		future.addListener(f -> {
			triggerDisconnectListeners();
		});
		return future;
	}

}
