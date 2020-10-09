package com.ggx.router.common.message.req;

import com.ggx.core.common.message.model.Message;

public class RouteMessageReq implements Message{
	
	// 传递的会话id
	private String tranferSessionId;
	
	//序列化方式
	private String serializeType;

	/* 消息标识 */
	private byte[] action;

	/* 消息体 */
	private byte[] message;

	public String getTranferSessionId() {
		return tranferSessionId;
	}

	public void setTranferSessionId(String tranferSessionId) {
		this.tranferSessionId = tranferSessionId;
	}

	public String getSerializeType() {
		return serializeType;
	}

	public void setSerializeType(String serializeType) {
		this.serializeType = serializeType;
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
