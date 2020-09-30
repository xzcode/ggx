package com.ggx.core.common.message.pingpong;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.pingpong.model.GGXPingPongInfo;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 内置pingpong处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class GGXPongResponseHandler implements MessageHandler<Pong>{
	
	protected GGXCoreConfig config;
	
	public GGXPongResponseHandler(GGXCoreConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<Pong> request) {
		GGXSession session = request.getSession();
		GGXPingPongInfo ggPingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGXPingPongInfo.class);
		if (ggPingPongInfo == null) {
			ggPingPongInfo = new GGXPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, ggPingPongInfo);
		}
		ggPingPongInfo.heartBeatLostTimesReset();
	}
	


}
