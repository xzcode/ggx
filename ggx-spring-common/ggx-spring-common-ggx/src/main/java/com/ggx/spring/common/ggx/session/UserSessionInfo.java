package com.ggx.spring.common.ggx.session;

public class UserSessionInfo {
	
	private String userId;
	private String token;
	private String sessionId;
	
	
	public UserSessionInfo(String userId, String token, String sessionId) {
		super();
		this.userId = userId;
		this.token = token;
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
