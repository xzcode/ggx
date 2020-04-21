package com.xzcode.ggcloud.session.group.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.request.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.xzcode.ggcloud.session.group.client.config.SessionGroupClientConfig;
import com.xzcode.ggcloud.session.group.common.constant.GGSessionGroupEventConstant;
import com.xzcode.ggcloud.session.group.common.message.resp.SessionGroupRegisterResp;

/**
 * 内置pingpong处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class SessionGroupRegisterRespHandler implements MessageDataHandler<SessionGroupRegisterResp>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionGroupRegisterRespHandler.class);
	
	protected SessionGroupClientConfig config;
	
	public SessionGroupRegisterRespHandler(SessionGroupClientConfig config) {
		this.config = config;
	}

	@Override
	public void handle(MessageData<SessionGroupRegisterResp> request) {
		GGSession session = request.getSession();
		SessionGroupRegisterResp resp = request.getMessage();
		//会话组注册成功
		if (resp.isSuccess()) {
			//添加会话到管理器
			this.config.getSessionGroupManager().addSession(this.config.getSessionGroupId(), session);
			//设置会话准备就绪
			session.setReady(true);
			
			//触发会话注册成功事件
			session.emitEvent(new EventData<Void>(session, GGSessionGroupEventConstant.SESSION_REGISTER_SUCCESS, null));
			
		}
	}
	


}
