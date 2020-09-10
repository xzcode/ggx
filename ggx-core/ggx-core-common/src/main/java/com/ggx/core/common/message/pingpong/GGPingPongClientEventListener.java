package com.ggx.core.common.message.pingpong;

import java.nio.charset.Charset;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.pingpong.model.Ping;
import com.ggx.core.common.message.pingpong.model.GGPingPongInfo;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;


public class GGPingPongClientEventListener implements EventListener<Void>, MakePackSupport{
	
	protected GGXCoreConfig config;
	
	protected static final AttributeKey<GGPingPongInfo> PING_PONG_INFO_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PING_INFO);
	
	public GGPingPongClientEventListener(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		GGXSession session = eventData.getSession();
		session.send(Ping.DEFAULT_INSTANT.getActionId());
	}

	@Override
	public Charset getCharset() {
		return config.getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return config.getSerializer();
	}


}
