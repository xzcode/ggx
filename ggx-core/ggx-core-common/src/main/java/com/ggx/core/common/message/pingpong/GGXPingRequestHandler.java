package com.ggx.core.common.message.pingpong;

import java.nio.charset.Charset;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.pingpong.model.GGXPingPongInfo;
import com.ggx.core.common.message.pingpong.model.Ping;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.session.GGXSession;

/**
 * 内置ping处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class GGXPingRequestHandler implements MessageHandler<Ping> , MakePackSupport{
	
	protected GGXCoreConfig config;
	
	public GGXPingRequestHandler(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<Ping> request) {
		GGXSession session = request.getSession();
		request.getSession().send(Pong.DEFAULT_INSTANT.getActionId(), null);
		
		GGXPingPongInfo pingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGXPingPongInfo.class);
		if (pingPongInfo == null) {
			pingPongInfo = new GGXPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, pingPongInfo);
		}
		pingPongInfo.heartBeatLostTimesReset();
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
