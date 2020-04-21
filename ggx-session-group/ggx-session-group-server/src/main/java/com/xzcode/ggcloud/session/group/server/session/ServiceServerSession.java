package com.xzcode.ggcloud.session.group.server.session;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.future.GGDefaultFuture;
import com.ggx.core.common.future.IGGFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.impl.AbstractAttrMapSession;
import com.xzcode.ggcloud.session.group.common.group.manager.GGSessionGroupManager;
import com.xzcode.ggcloud.session.group.common.message.resp.DataTransferResp;

import io.netty.channel.Channel;

/**
 * 业务服务端session
 *
 * @author zai
 * 2020-04-09 10:11:53
 */
public class ServiceServerSession extends AbstractAttrMapSession<GGConfig>{
	
	
	
	//会话组管理器
	protected GGSessionGroupManager sessionGroupManager;

	public ServiceServerSession(String sessionId, String groupId, GGSessionGroupManager sessionGroupManager,GGConfig config) {
		super(sessionId, config);
		this.sessionGroupManager = sessionGroupManager;
		this.groupId = groupId;
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
	public IGGFuture send(Pack pack) {
		DataTransferResp resp = new DataTransferResp();
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setTranferSessionId(this.getSessonId());
		return sessionGroupManager.sendToRandomOne(groupId, makePack(new MessageData<>(resp.getActionId(), resp)));
	}

	@Override
	public IGGFuture disconnect() {
		
		triggerDisconnectListeners();
		
		//触发断开连接事件
		this.emitEvent(new EventData<>(this, GGEvents.Connection.CLOSED, null));
		
		GGDefaultFuture future = new GGDefaultFuture();
		future.setSession(this);
		future.setDone(true);
		future.setSuccess(true);
		return future;
	}

	

}
