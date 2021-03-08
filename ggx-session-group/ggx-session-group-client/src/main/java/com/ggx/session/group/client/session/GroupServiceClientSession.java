package com.ggx.session.group.client.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.GGXFuture;
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

	public GroupServiceClientSession(String sessionId, GGXSession groupSession, GGXCoreConfig config) {
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
	
	/**
	 * 启动超时自动更新任务，用于避免session超时
	 *
	 * 2021-03-09 00:14:34
	 */
	public void startUpdateExpireTask() {
		this.schedule(30L * 1000L, () -> {
			if(!this.disconnected) {
				this.updateExpire();
				this.startUpdateExpireTask();
			}
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
		
		
		DataTransferReq resp = new DataTransferReq();
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
