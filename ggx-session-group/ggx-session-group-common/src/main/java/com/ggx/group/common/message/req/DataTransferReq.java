package com.ggx.group.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.group.common.constant.GGSesssionGroupConstant;

/**
 * 数据传输请求
 *
 * @author zai 2020-04-08 10:31:48
 */
public class DataTransferReq extends AbstractMessage {

	public static final String ACTION = GGSesssionGroupConstant.ACTION_ID_PREFIX + "DATA.TRANSFER.REQ";

	// 传递的会话id
	private String tranferSessionId;
	
	//序列化方式
	private String serializeType;

	/* 消息标识 */
	private byte[] action;

	/* 消息体 */
	private byte[] message;

	public DataTransferReq() {

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

	
}
