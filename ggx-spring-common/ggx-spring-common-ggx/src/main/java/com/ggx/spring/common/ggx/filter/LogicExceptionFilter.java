package com.ggx.spring.common.ggx.filter;

import org.springframework.beans.factory.annotation.Autowired;

import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.message.receive.controller.model.ControllerMethodInfo;
import com.ggx.server.starter.GGXServer;
import com.ggx.spring.common.base.exception.LogicException;
import com.ggx.spring.common.ggx.message.BaseResp;
import com.ggx.util.logger.GGXLogUtil;

public class LogicExceptionFilter implements ReceiveMessageFilter{
	
	@Autowired
	private GGXServer ggxserver;

	@Override
	public void doFilter(MessageData data, FilterChain<MessageData> filterChain)  throws Throwable{
		try {
			filterChain.doFilter(data);
		} catch (Throwable e) {
			if (e instanceof LogicException) {
				LogicException le = (LogicException) e;
				MessageControllerManager messageControllerManager = ggxserver.getMessageControllerManager();
				ControllerMethodInfo methodInfo = messageControllerManager.getMethodInfo(data.getAction());
				Class<?> returnClass = methodInfo.getReturnClass();
				BaseResp baseResp;
				try {
					baseResp = (BaseResp) returnClass.newInstance();
					baseResp.setRespSuccess(false);
					baseResp.setRespMsg(le.getMessage());
					baseResp.setRespCode(le.getCode());
					data.getSession().send(baseResp);
				} catch (Exception e1) {
					GGXLogUtil.getLogger(this).error("Cast Error!", e1);
				}
				return;
			}
			throw e;
		}
	}

}
