package com.ggx.group.common.session;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.session.factory.DefaultChannelSessionFactory;
import com.ggx.core.common.session.impl.DefaultChannelSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class SessionGroupSessionFactory extends DefaultChannelSessionFactory{

	public SessionGroupSessionFactory(GGXCoreConfig config) {
		super(config);
	}

	@Override
	public void channelActive(Channel channel) {
		super.channelActive(channel);
		DefaultChannelSession session = (DefaultChannelSession) channel.attr(AttributeKey.valueOf(DefaultChannelAttributeKeys.SESSION)).get();
		session.setReady(false);
	}

	
}
