package com.ggx.common.controller.resp;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 事件订阅推送
 *
 * @author zai
 * 2020-04-07 11:31:03
 */
public class EventSubscribeResp  extends AbstractMessage {
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
