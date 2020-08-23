package com.ggx.core.common.message.pingpong;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.pingpong.model.GGPingPongInfo;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.action.MessageHandler;

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
	
	protected static final AttributeKey<GGPingPongInfo> PING_PONG_INFO_KEY = AttributeKey.valueOf(DefaultChannelAttributeKeys.PING_INFO);
	
	
	public GGPongResponseHandler(GGXCoreConfig config) {
		this.config = config;
	}



	@Override
	public void handle(MessageData<Pong> request) {
		Channel channel = request.getChannel();
		GGPingPongInfo gGPingPongInfo = channel.attr(PING_PONG_INFO_KEY).get();
		if (gGPingPongInfo == null) {
			gGPingPongInfo = new GGPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			channel.attr(PING_PONG_INFO_KEY).set(gGPingPongInfo);
		}
		gGPingPongInfo.heartBeatLostTimesReset();
	}
	


}
