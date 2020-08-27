package com.ggx.core.common.message.pingpong;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.pingpong.model.GGPingPongInfo;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 内置pingpong处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class GGPongResponseHandler implements MessageHandler<Pong>{
	
	protected GGXCoreConfig config;
	
	public GGPongResponseHandler(GGXCoreConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<Pong> request) {
		GGSession session = request.getSession();
		GGPingPongInfo ggPingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGPingPongInfo.class);
		if (ggPingPongInfo == null) {
			ggPingPongInfo = new GGPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, ggPingPongInfo);
		}
		ggPingPongInfo.heartBeatLostTimesReset();
	}
	


}
