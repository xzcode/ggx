package com.ggx.core.common.event.model;

import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;

public class EventData<T> {
	protected String event;
	protected GGXSession session;
	protected Channel channel;
	protected T data;
	
	
	public EventData(String event) {
		this.event = event;
	}
	public EventData(String event, T data) {
		this.event = event;
		this.data = data;
	}
	public EventData(GGXSession session, String event) {
		this.session = session;
		this.event = event;
	}
	public EventData(GGXSession session, String event, T data) {
		this.session = session;
		this.event = event;
		this.data = data;
	}
	public EventData(GGXSession session, String event, T data, Channel channel) {
		this.session = session;
		this.data = data;
		this.event = event;
		this.channel = channel;
	}
	public GGXSession getSession() {
		return session;
	}
	public void setSession(GGXSession session) {
		this.session = session;
	}
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
}
