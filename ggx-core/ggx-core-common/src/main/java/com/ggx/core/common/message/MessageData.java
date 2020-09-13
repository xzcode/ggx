package com.ggx.core.common.message;

import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;

/**
 * 消息模型
 * @param <T>
 * 
 * @author zai
 * 2019-12-01 17:15:54
 */
public class MessageData<T extends Message> {
	
	//会话对象
	private GGXSession session;

	//发送消息标识
	private String action;
	
	//消息体
	private T message;
	
	//通道
	private Channel channel;
	
	//是否穿越过滤器
	private boolean crossFilters;
	
	
	
	public MessageData(GGXSession session, String action, T message) {
		this.session = session;
		this.action = action;
		this.message = message;
	}
	public MessageData(GGXSession session, T message) {
		this.session = session;
		this.action = message.getActionId();
		this.message = message;
	}
	
	public MessageData(String action, T message) {
		this.action = action;
		this.message = message;
	}
	
	public MessageData(T message) {
		this.action = message.getActionId();
		this.message = message;
	}

	public String getAction() {
		return action;
	}

	public T getMessage() {
		return message;
	}

	public GGXSession getSession() {
		return session;
	}
	
	public void setSession(GGXSession session) {
		this.session = session;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMessage(T message) {
		this.message = message;
	}
	
	public boolean isCrossFilters() {
		return crossFilters;
	}
	
	public void setCrossFilters(boolean crossFilters) {
		this.crossFilters = crossFilters;
	}
	
}
