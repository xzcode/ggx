package com.ggx.core.common.session.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;

public class VirtualSession extends AbstractAttrMapSession<GGXCoreConfig>{
	
	protected GGXSession realSession;

	public VirtualSession(GGXSession realSession, GGXCoreConfig config) {
		super(realSession.getSessionId(), config);
		this.realSession = realSession;
		setReady(true);
	}

	@Override
	public Channel getChannel() {
		return realSession.getChannel();
	}

	@Override
	public void setChannel(Channel channel) {
		realSession.setChannel(channel);
	}


	@Override
	public GGXFuture send(Pack pack) {
		return realSession.send(pack);
	}

	@Override
	public GGXFuture disconnect() {
		
		triggerDisconnectListeners();
		
		//触发断开连接事件
		this.emitEvent(new EventData<>(this, GGXCoreEvents.Connection.CLOSED, null));
		
		GGXDefaultFuture future = new GGXDefaultFuture();
		future.setSession(this);
		future.setDone(true);
		future.setSuccess(true);
		return future;
	}

	
	

}
