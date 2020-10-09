package com.ggx.group.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 会话组注册请求
 *
 * @author zai 2020-04-07 16:45:30
 */
public class SessionGroupRegisterReq extends AbstractMessage{

	// 会话组id
	private String groupId;

	public SessionGroupRegisterReq() {
	}

	public SessionGroupRegisterReq(String groupId) {
		super();
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
