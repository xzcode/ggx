package com.ggx.common.message.resp;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.message.model.Message;

/**
 * 事件订阅推送
 *
 * @author zai
 * 2020-04-07 11:31:03
 */
public class EventSubscribeResp implements Message {
	
	public static final String ACTION_ID = EventbusConstant.ACTION_ID_PREFIX + "EVENT.SUB.RESP";
	
	@Override
	public String getActionId() {
		return ACTION_ID;
	}
	
	/**
	 * 是否订阅成功
	 */
	private boolean success;
	

	public EventSubscribeResp() {
	}


	public EventSubscribeResp(boolean success) {
		this.success = success;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}

	
}
