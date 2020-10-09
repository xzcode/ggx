package com.ggx.core.common.message.pingpong;

import java.nio.charset.Charset;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.pingpong.model.Ping;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;


public class GGXPingPongClientEventListener implements EventListener<Void>, MakePackSupport{
	
	protected GGXCoreConfig config;
	protected ActionIdCacheManager actionIdCacheManager;
	
	public GGXPingPongClientEventListener(GGXCoreConfig config) {
		super();
		this.config = config;
		this.actionIdCacheManager = config.getActionIdCacheManager();
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		GGXSession session = eventData.getSession();
		session.send(Ping.class);
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
