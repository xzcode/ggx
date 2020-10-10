package com.ggx.router.common.message.resp;

import com.ggx.core.common.message.model.Message;

/**
 * 路由会话断开连接传递请求
 *
 * @author zai
 * 2020-05-20 11:50:25
 */
public class RouterRedirectMessageToOtherRouterServicesResp implements Message {

	// 会话id
	private String tranferSessionId;
	
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


	public String getTranferSessionId() {
		return tranferSessionId;
	}


	public void setTranferSessionId(String sessionId) {
		this.tranferSessionId = sessionId;
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
