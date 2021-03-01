package com.ggx.router.common.message.resp;

import com.ggx.core.common.message.model.Message;

public class RouteMessageResp implements Message {

	// 传递的会话id
	private String tranferSessionId;
	
	/* 请求序列 */
	private int requestSeq;

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
	
	public int getRequestSeq() {
		return requestSeq;
	}
	
	public void setRequestSeq(int requestSeq) {
		this.requestSeq = requestSeq;
	}

}
