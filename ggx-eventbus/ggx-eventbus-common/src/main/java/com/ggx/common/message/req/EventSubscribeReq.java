package com.ggx.common.message.req;

import java.util.List;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.message.model.Message;

/**
 * 事件订阅请求
 *
 * @author zai
 * 2020-04-06 18:50:10
 */
public class EventSubscribeReq implements Message {
	
	public static final String ACTION_ID = EventbusConstant.ACTION_ID_PREFIX + "EVENT.SUB.REQ";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	//事件id
	private List<String> eventIds;
	

	public EventSubscribeReq() {
	}


	public EventSubscribeReq(List<String> eventIds) {
		super();
		this.eventIds = eventIds;
	}


	public List<String> getEventIds() {
		return eventIds;
	}


	public void setEventIds(List<String> eventIds) {
		this.eventIds = eventIds;
	}
	
	

	
}
