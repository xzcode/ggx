package com.ggx.core.common.message.pingpong;

import java.nio.charset.Charset;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.pingpong.model.GGXPingPongInfo;
import com.ggx.core.common.message.pingpong.model.Ping;
import com.ggx.core.common.message.pingpong.model.Pong;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;

/**
 * 内置ping pong控制器
 *
 * @author zai
 * 2020-10-09 18:53:45
 */
public class GGXPingPongController implements MakePackSupport{
	
	protected GGXCoreConfig config;
	protected ActionIdCacheManager actionIdCacheManager;
	
	public GGXPingPongController(GGXCoreConfig config) {
		this.config = config;
		this.actionIdCacheManager = config.getActionIdCacheManager();
	}

	@GGXAction
	public void ping(Ping ping, GGXSession session) {
		session.send(this.actionIdCacheManager.get(Pong.class), null);
		
		GGXPingPongInfo pingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGXPingPongInfo.class);
		if (pingPongInfo == null) {
			pingPongInfo = new GGXPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, pingPongInfo);
		}
		pingPongInfo.heartBeatLostTimesReset();
	}
	
	@GGXAction
	public void pong(Pong pong, GGXSession session) {
		GGXPingPongInfo ggPingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGXPingPongInfo.class);
		if (ggPingPongInfo == null) {
			ggPingPongInfo = new GGXPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, ggPingPongInfo);
		}
		ggPingPongInfo.heartBeatLostTimesReset();
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
