package com.ggx.rpc.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.core.common.message.model.Message;
import com.ggx.rpc.common.constant.RpcConstant;

/**
 * 事件订发布请求
 *
 * @author zai 2020-04-06 18:50:10
 */
public class RpcReq extends AbstractMessage{


	// 事件id
	private String eventId;

	// 事件数据
	private byte[] eventData;

	public RpcReq() {

	}

	public RpcReq(String eventId, byte[] eventData) {
		super();
		this.eventId = eventId;
		this.eventData = eventData;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public byte[] getEventData() {
		return eventData;
	}

	public void setEventData(byte[] eventData) {
		this.eventData = eventData;
	}

}
