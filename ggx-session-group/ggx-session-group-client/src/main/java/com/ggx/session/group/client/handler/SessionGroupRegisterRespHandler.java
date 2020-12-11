package com.ggx.session.group.client.handler;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.constant.GGSessionGroupEventConstant;
import com.ggx.group.common.message.resp.SessionGroupRegisterResp;
import com.ggx.session.group.client.config.SessionGroupClientConfig;

/**
 * 内置pingpong处理器
 * 
 * @author zai
 * 2020-01-16 17:04:11
 */
public class SessionGroupRegisterRespHandler {
	
	protected SessionGroupClientConfig config;
	
	public SessionGroupRegisterRespHandler(SessionGroupClientConfig config) {
		this.config = config;
	}

	@GGXAction
	public void handle(SessionGroupRegisterResp resp, GGXSession session) {
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
