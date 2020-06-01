package com.ggx.router.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 路由会话断开连接传递请求
 *
 * @author zai
 * 2020-05-20 11:50:25
 */
public class RouterRedirectMessageToOtherRouterServicesResp implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + ".REDIRECT.MESSAGE.TO.OTHER.ROUTER.SERVICE.RESP";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 会话id
	private String sessionId;
	
	//服务id
	private String serviceId;
	
	
	/* 消息标识 */
	private byte[] action;

	/* 消息体 */
	private byte[] message;
	
	/* 包操序列化方式*/
	private String serializeType;
	
	

	public RouterRedirectMessageToOtherRouterServicesResp() {

	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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


	public String getSerializeType() {
		return serializeType;
	}


	public void setSerializeType(String serializeType) {
		this.serializeType = serializeType;
	}
	
	
	

	
}
