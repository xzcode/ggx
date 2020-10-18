package com.ggx.common.controller.resp;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 事件订发布响应
 *
 * @author zai 2020-04-06 18:50:10
 */
public class EventPublishResp  extends AbstractMessage {


	/**
	 * 是否发布成功
	 */
	private boolean success;

	public EventPublishResp() {

	}

	public EventPublishResp(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
