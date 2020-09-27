package com.ggx.core.common.message.pingpong;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.EventTask;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.pingpong.model.GGPingPongInfo;
import com.ggx.core.common.session.GGSession;


public class GGPingPongServerEventListener implements EventListener<Void>{
	
	protected GGXCoreConfig config;
	
	
	public GGPingPongServerEventListener(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		GGSession session = eventData.getSession();
		GGPingPongInfo pingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO, GGPingPongInfo.class);
		if (pingPongInfo == null) {
			pingPongInfo = new GGPingPongInfo(config.getPingPongLostTimes(), config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, pingPongInfo);
		}
		pingPongInfo.heartBeatLostTimesIncrease();
		
		//超过心跳丢失次数，断开连接
		if (pingPongInfo.isHeartBeatLost()) {
			session.disconnect();
			session.submitTask(new EventTask(session, GGXCoreEvents.HeartBeat.LOST, "Heart beat lost!", config));
			return;
		}
	}


}
