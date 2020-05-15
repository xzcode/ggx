package com.ggx.session.group.client.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageDataHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.group.common.message.req.AuthReq;
import com.ggx.group.common.message.req.SessionGroupRegisterReq;
import com.ggx.group.common.message.resp.AuthResp;
import com.ggx.session.group.client.config.SessionGroupClientConfig;

/**
 * 客户端认证请求
 *
 * @author zai 2020-04-07 10:57:11
 */
public class AnthRespHandler implements MessageDataHandler<AuthResp> {

	private SessionGroupClientConfig config;

	public AnthRespHandler(SessionGroupClientConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void handle(MessageData<AuthResp> request) {
		AuthResp resp = request.getMessage();
		GGSession session = request.getSession();
		if (resp.isSuccess()) {
			// 认证成功后，进行会话组注册请求
			session.send(SessionGroupRegisterReq.ACTION_ID, new SessionGroupRegisterReq(config.getSessionGroupId()));
			return;
		}
		//认证失败
		GGLoggerUtil.getLogger(this).error("SessionGroupClient Auth Failed!!");
		session.schedule(5000, () -> {
			session.send(new AuthReq(config.getAuthToken()));
		});
		
	}

}
