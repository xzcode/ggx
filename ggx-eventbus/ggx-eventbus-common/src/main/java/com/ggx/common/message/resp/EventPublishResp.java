package com.ggx.common.message.resp;

import com.ggx.common.constant.EventbusConstant;
import com.ggx.core.common.message.model.Message;

/**
 * 事件订发布响应
 *
 * @author zai 2020-04-06 18:50:10
 */
public class EventPublishResp implements Message {

	public static final String ACTION_ID = EventbusConstant.ACTION_ID_PREFIX + "EVENT.PUB.RESP";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

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
