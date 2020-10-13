package com.ggx.core.common.message;

import com.ggx.core.common.session.GGXSession;

/**
 * 消息模型
 * @param <T>
 * 
 * @author zai
 * 2019-12-01 17:15:54
 */
public class MessageData {
	
	//会话对象
	private GGXSession session;

	//发送消息标识
	private String action;
	
	//消息体
	private Object message;
	
	//是否穿越过滤器
	private boolean crossFilters;
	
	
	
	public MessageData(GGXSession session, String action, Object message) {
		this.session = session;
		this.action = action;
		this.message = message;
	}
	public MessageData(GGXSession session, Object message) {
		this.session = session;
		this.action = session.getActionIdCacheManager().get(message.getClass());
		this.message = message;
	}
	
	public MessageData(String action, Object message) {
		this.action = action;
		this.message = message;
	}
	
	public String getAction() {
		return action;
	}

	public Object getMessage() {
		return message;
	}

	public GGXSession getSession() {
		return session;
	}
	
	public void setSession(GGXSession session) {
		this.session = session;
	}
	

	public void setAction(String action) {
		this.action = action;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
	
	public boolean isCrossFilters() {
		return crossFilters;
	}
	
	public void setCrossFilters(boolean crossFilters) {
		this.crossFilters = crossFilters;
	}
	
}
