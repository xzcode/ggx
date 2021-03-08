package com.ggx.group.server.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.AbstractAttrMapSession;
import com.ggx.group.common.message.resp.DataTransferResp;

import io.netty.channel.Channel;

/**
 * 业务服务端session
 *
 * @author zai
 * 2020-04-09 10:11:53
 */
public class GroupServiceServerSession extends AbstractAttrMapSession<GGXCoreConfig>{
	
	protected GGXSession groupSession;

	public GroupServiceServerSession(String sessionId, GGXSession groupSession, GGXCoreConfig config) {
		super(sessionId, config);
		this.groupSession = groupSession;
		setReady(true);
		
		groupSession.addDisconnectListener(s -> {
			this.disconnect();
		});
		groupSession.addUpdateExpireListener(s -> {
			this.updateExpire();
		});
	}
	
	@Override
	public TaskExecutor getTaskExecutor() {
		return this.groupSession.getTaskExecutor();
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
		
		DataTransferResp resp = new DataTransferResp();
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setRequestSeq(pack.getRequestSeq());
		
		return groupSession.send(resp);
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
