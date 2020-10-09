package com.ggx.session.group.client.handler;

import com.ggx.core.common.message.receive.controller.annotation.GGXAction;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.req.SessionGroupRegisterReq;
import com.ggx.group.common.message.resp.AuthResp;
import com.ggx.session.group.client.config.SessionGroupClientConfig;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class AnthRespHandler {

	private SessionGroupClientConfig config;

	public AnthRespHandler(SessionGroupClientConfig config) {
		super();
		this.config = config;
	}

	@GGXAction
	public void handle(AuthResp resp, GGXSession session) {
		if (resp.isSuccess()) {
			// 认证成功后，进行会话组注册请求
			session.send(new SessionGroupRegisterReq(config.getSessionGroupId()));
			return;
		}
		//认证失败
		GGXLogUtil.getLogger(this).error("SessionGroupClient Auth Failed!!");
		session.schedule(5000, () -> {
			session.send(new AuthReq(config.getAuthToken()));
		});
		
	}

}
