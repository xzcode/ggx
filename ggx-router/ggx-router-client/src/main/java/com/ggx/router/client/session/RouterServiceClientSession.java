package com.ggx.router.client.session;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.event.GGEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.future.GGDefaultFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.impl.AbstractAttrMapSession;
import com.ggx.session.group.client.session.GroupServiceClientSession;

import io.netty.channel.Channel;

/**
 * 路由业务服务端会话
 *
 * @author zai
 * 2020-05-11 12:30:35
 */
public class RouterServiceClientSession  extends AbstractAttrMapSession<GGConfig>{

	/**
	 * 会话组业务服务端会话对象
	 */
	protected GroupServiceClientSession groupServiceClientSession;
	
	public RouterServiceClientSession(GroupServiceClientSession groupServiceClientSession, String sessionId, GGConfig config) {
		super(sessionId, config);
		this.groupServiceClientSession = groupServiceClientSession;
		setReady(true);
	}

	@Override
	public Channel getChannel() {
		return null;
	}

	@Override
	public void setChannel(Channel channel) {
		
	}
	
	public GGFuture send(Pack pack) {
		return groupServiceClientSession.send(pack);
	}
	
	public GGFuture disconnect() {
		
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
