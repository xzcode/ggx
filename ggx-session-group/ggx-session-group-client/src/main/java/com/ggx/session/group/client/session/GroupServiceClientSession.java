package com.ggx.session.group.client.session;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.AbstractAttrMapSession;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.req.DataTransferReq;

import io.netty.channel.Channel;

/**
 * 业务客户端session
 *
 * @author zai
 * 2020-04-13 17:01:32
 */
public class GroupServiceClientSession extends AbstractAttrMapSession<GGXCoreConfig>{
	
	//会话组id
	protected String groupId;
	
	//会话组管理器
	protected GGSessionGroupManager sessionGroupManager;

	protected GGXSession groupSession;

	public GroupServiceClientSession(String sessionId, GGXSession groupSession,String groupId, GGSessionGroupManager sessionGroupManager,GGXCoreConfig config) {
		super(sessionId, config);
		this.sessionGroupManager = sessionGroupManager;
		this.groupId = groupId;
		this.groupSession = groupSession;
		setReady(true);
	}

	@Override
	public Channel getChannel() {
		return null;
	}

	@Override
	public void setChannel(Channel channel) {
		
	}


	@Override
	public GGXFuture<?> send(Pack pack) {
		
		
		DataTransferReq resp = new DataTransferReq();
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		
		if (this.groupSession == null || this.groupSession.isExpired()) {
			this.groupSession = sessionGroupManager.getRandomOne(groupId);
		}
		if (this.groupSession == null) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		return groupSession.send(resp);
	}

	@Override
	public GGXFuture<?> disconnect() {
		
		triggerDisconnectListeners();
		
		//触发断开连接事件
		this.emitEvent(new EventData<>(this, GGXCoreEvents.Connection.CLOSED, null));
		
		GGXCoreFuture<?> future = new GGXCoreFuture<>();
		future.setSession(this);
		future.setDone(true);
		future.setSuccess(true);
		return future;
	}

	
	

}
