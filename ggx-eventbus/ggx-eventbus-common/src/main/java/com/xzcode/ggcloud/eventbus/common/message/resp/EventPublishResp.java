package com.xzcode.ggcloud.eventbus.common.message.resp;

import com.ggx.core.common.message.model.IMessage;
import com.xzcode.ggcloud.eventbus.common.constant.EventbusConstant;

/**
 * 事件订发布响应
 *
 * @author zai 2020-04-06 18:50:10
 */
public class EventPublishResp implements IMessage {

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
