package com.xzcode.ggcloud.eventbus.common.message.resp;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.eventbus.common.constant.EventbusConstant;

/**
 * 事件消息推送
 *
 * @author zai
 * 2020-04-07 11:34:50
 */
public class EventMessageResp implements IMessage {

	public static final String ACTION_ID = EventbusConstant.ACTION_ID_PREFIX + "EVENT.MESSAGE.RESP";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 事件id
	private String eventId;
	
	// 订阅器id
	private String subscriberId;

	// 事件数据
	private byte[] eventData;

	public EventMessageResp() {

	}

	public EventMessageResp(String eventId, String subscriberId,byte[] eventData) {
		super();
		this.eventId = eventId;
		this.eventData = eventData;
		this.subscriberId = subscriberId;
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

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	

}
