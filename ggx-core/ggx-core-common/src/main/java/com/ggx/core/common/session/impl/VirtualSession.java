package com.ggx.core.common.session.impl;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;

import io.netty.channel.Channel;

public class VirtualSession extends AbstractAttrMapSession<GGXCoreConfig>{
	
	protected SessionManager realSessionManager;
	
	protected GGXSession realSession;

	public VirtualSession(String sessionId, GGXSession realSession, SessionManager realSessionManager, GGXCoreConfig config) {
		super(sessionId, config);
		this.realSession = realSession;
		this.realSessionManager = realSessionManager;
		setReady(true);
	}
	
	@Override
	public TaskExecutor getTaskExecutor() {
		return this.realSession.getTaskExecutor();
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
	public GGXFuture<?> send(Pack pack) {
		return realSession.send(pack);
	}

	@Override
	public GGXFuture<?> disconnect() {
		
		GGXCoreFuture<?> future = new GGXCoreFuture<>();
		this.submitTask(() -> {
			this.disconnected = true;
			
			triggerDisconnectListeners();
			
			//触发断开连接事件
			this.emitEvent(new EventData<>(this, GGXCoreEvents.Connection.CLOSED, null));
			
			future.setSession(this);
			future.setDone(true);
			future.setSuccess(true);
		});
		return future;
	}

	
	

}
