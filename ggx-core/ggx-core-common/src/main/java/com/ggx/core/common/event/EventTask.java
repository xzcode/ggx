package com.ggx.core.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.Channel;

/**
 * 事件任务
 * 
 * @author zai
 * 2019-03-16 18:53:59
 */
public class EventTask implements Runnable{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(EventTask.class);
	
	private EventManager eventManager;
	
	/**
	 * Channel对象
	 */
	private Channel channel;
	
	/**
	 * session对象
	 */
	private GGXSession session;
	
	/**
	 * event标识
	 */
	private String event;
	
	/**
	 * 消息对象
	 */
	private Object message;
	
	
	public EventTask(String event, Object message, EventManager eventManager) {
		this.event = event;
		this.message = message;
		this.eventManager = eventManager;
	}
	

	public EventTask(GGXSession session, String event, Object message, EventManager eventManager) {
		this.session = session;
		this.event = event;
		this.message = message;
		this.eventManager = eventManager;
	}
	public EventTask(GGXSession session, String event, Object message, EventManager eventManager, Channel channel) {
		this.session = session;
		this.event = event;
		this.message = message;
		this.eventManager = eventManager;
		this.channel = channel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		EventData eventData = new EventData(session, event, message, channel);
		try {
			this.eventManager.emitEvent(eventData);	
		} catch (Exception e) {
			LOGGER.error("EventTask Error!!", e);
		}
	}

}
