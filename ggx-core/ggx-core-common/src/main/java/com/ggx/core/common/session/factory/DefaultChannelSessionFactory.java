package com.ggx.core.common.session.factory;

import java.net.InetSocketAddress;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.id.SessionIdGenerator;
import com.ggx.core.common.session.impl.DefaultChannelSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 默认通道会话工厂
 * 
 * 
 * @author zai 2019-11-16 11:02:22
 */
public class DefaultChannelSessionFactory implements ChannelSessionFactory {

	protected GGXCoreConfig config;

	protected AttributeKey<GGXSession> sessAttributeKey = AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION);

	private SessionIdGenerator sessionIdGenerator;

	public DefaultChannelSessionFactory(GGXCoreConfig config) {
		super();
		this.config = config;
		this.sessionIdGenerator = this.config.getSessionIdGenerator();
	}

	@Override
	public GGXSession getSession(Channel channel) {
		GGXSession session = channel.attr(sessAttributeKey).get();
		session.updateExpire();
		return session;
	}

	@Override
	public void channelActive(Channel channel) {
		// 初始化session
		DefaultChannelSession session = new DefaultChannelSession(this.sessionIdGenerator.generateSessionId(), channel, config);
		InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
		session.setHost(remoteAddress.getHostString());
		session.setPort(remoteAddress.getPort());
		session.setReady(true);
		channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).set(session);

		config.getSessionManager().addSessionIfAbsent(session);

	}

	@Override
	public void channelInActive(Channel channel) {
		GGXSession session = getSession(channel);
		if (session != null) {
			session.disconnect();
		}
	}

}
