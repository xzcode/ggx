package com.ggx.group.server.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.AbstractAttrMapSession;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.group.common.message.resp.DataTransferResp;

import io.netty.channel.Channel;

/**
 * 业务服务端session
 *
 * @author zai
 * 2020-04-09 10:11:53
 */
public class GroupServiceServerSession extends AbstractAttrMapSession<GGXCoreConfig>{
	
	//会话组管理器
	protected GGSessionGroupManager sessionGroupManager;
	
	protected GGXSession groupSession;

	public GroupServiceServerSession(String sessionId, String groupId, GGSessionGroupManager sessionGroupManager,GGXCoreConfig config) {
		super(sessionId, config);
		this.sessionGroupManager = sessionGroupManager;
		this.groupId = groupId;
		this.groupSession = sessionGroupManager.getRandomOne(groupId);
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
	public GGXFuture send(Pack pack) {
		
		DataTransferResp resp = new DataTransferResp();
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
