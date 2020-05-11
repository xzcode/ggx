package com.ggx.router.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 数据路由请求
 *
 * @author zai
 * 2020-05-11 11:28:35
 */
public class RouterMessageReq implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + "ROUTE.MSG.REQ";

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

	public RouterMessageReq() {

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
	
	public String getRouteSessionId() {
		return routeSessionId;
	}
	
	public void setRouteSessionId(String tranferSessionId) {
		this.routeSessionId = tranferSessionId;
	}

}
