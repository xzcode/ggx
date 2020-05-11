package com.ggx.router.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 消息路由推送
 *
 * @author zai
 * 2020-05-11 11:14:19
 */
public class RouterMessageResp implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + "ROUTE.MSG.RESP";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	
	// 路由的会话id
	private String routeSessionId;

	/* 消息标识 */
	private byte[] action;

	/* 消息体 */
	private byte[] message;
	
	

	public RouterMessageResp() {
	}
	
	

	public String getRouteSessionId() {
		return routeSessionId;
	}



	public void setRouteSessionId(String routeSessionId) {
		this.routeSessionId = routeSessionId;
	}



	public byte[] getAction() {
		return action;
	}

	public void setAction(byte[] action) {
		this.action = action;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}


}
