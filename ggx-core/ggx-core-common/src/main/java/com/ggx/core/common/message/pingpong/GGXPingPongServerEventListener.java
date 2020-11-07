package com.ggx.core.common.message.pingpong;

import com.ggx.core.common.channel.DefaultChannelAttributeKeys;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.pingpong.model.GGXPingPongInfo;
import com.ggx.core.common.session.GGXSession;

import io.netty.util.AttributeKey;

public class GGXPingPongServerEventListener implements EventListener<Void> {

	protected GGXCoreConfig config;

	protected static final AttributeKey<GGXPingPongInfo> PING_PONG_INFO_KEY = AttributeKey
			.valueOf(DefaultChannelAttributeKeys.PING_INFO);

	public GGXPingPongServerEventListener(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void onEvent(EventData<Void> eventData) {
		GGXSession session = eventData.getSession();
		GGXPingPongInfo pingPongInfo = session.getAttribute(DefaultChannelAttributeKeys.PING_INFO,
				GGXPingPongInfo.class);
		if (pingPongInfo == null) {
			pingPongInfo = new GGXPingPongInfo(0, config.getPingPongMaxLoseTimes());
			session.addAttribute(DefaultChannelAttributeKeys.PING_INFO, pingPongInfo);
		}
		pingPongInfo.heartBeatLostTimesIncrease();

		// 超过心跳丢失次数，断开连接
		if (pingPongInfo.isHeartBeatLost()) {
			session.disconnect();
			session.emitEvent(new EventData<>(session, GGXCoreEvents.HeartBeat.LOST, "Heart beat lost!"));
			return;
		}
	}

}
