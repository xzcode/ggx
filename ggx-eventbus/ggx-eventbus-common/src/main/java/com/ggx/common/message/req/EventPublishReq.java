package com.ggx.common.message.req;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.message.model.Message;

/**
 * 事件订发布请求
 *
 * @author zai 2020-04-06 18:50:10
 */
public class EventPublishReq implements Message {

	public static final String ACTION_ID = EventbusConstant.ACTION_ID_PREFIX + "EVENT.PUB.REQ";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 事件id
	private String eventId;

	// 事件数据
	private byte[] eventData;

	public EventPublishReq() {

	}

	public EventPublishReq(String eventId, byte[] eventData) {
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
