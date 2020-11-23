package com.ggx.spring.common.base.subscribction;

import com.ggx.common.message.EventbusMessage;

public class DisconnectRepeatLoginUserEvent implements EventbusMessage{
	
	private String userId;
	private String sessionId;
	
	public DisconnectRepeatLoginUserEvent() {
	}
	public DisconnectRepeatLoginUserEvent(String userId, String sessionId) {
		this.userId = userId;
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
	

}
