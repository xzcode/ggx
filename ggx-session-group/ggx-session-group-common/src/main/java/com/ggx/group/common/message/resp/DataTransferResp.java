package com.ggx.group.common.message.resp;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 数据传输推送
 *
 * @author zai
 * 2020-04-08 10:32:51
 */
public class DataTransferResp extends AbstractMessage {
	
	/* 请求序列 */
	private int requestSeq;

	/* 消息标识 */
	private byte[] action;

	/* 消息体 */
	private byte[] message;

	public DataTransferResp() {

	}
	
	public int getRequestSeq() {
		return requestSeq;
	}
	
	public void setRequestSeq(int requestSeq) {
		this.requestSeq = requestSeq;
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
