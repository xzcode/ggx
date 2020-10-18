package com.ggx.eventbus.server.subscription;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.common.controller.resp.EventMessageResp;
import com.ggx.core.common.session.GGXSession;
import com.ggx.eventbus.server.config.EventbusServerConfig;

/**
 * 事件订阅处理器
 *
 * @author zai
 * 2020-04-07 11:18:51
 */
public class SubscriptionManager {
	
	private EventbusServerConfig config;
	
	
	//事件订阅集合
	private Map<String, Subscription> subscriptions = new ConcurrentHashMap<String, Subscription>();
	
	
	public SubscriptionManager(EventbusServerConfig config) {
		super();
		this.config = config;
	}

	/**
	 * 添加订阅
	 *
	 * @param eventId
	 * @param session
	 * @author zai
	 * 2020-04-07 11:45:20
	 */
	public void addSubscription(String eventId, GGXSession session) {
		Subscription subscription = subscriptions.get(eventId);
		if (subscription == null) {
			subscription = new Subscription(eventId, this.config);
			Subscription putIfAbsent = subscriptions.putIfAbsent(eventId, subscription);
			if (putIfAbsent != null) {
				subscription = putIfAbsent;
			}
		}
		subscription.addSubscription(session);
	}
	
	/**
	 * 添加多事件订阅
	 *
	 * @param eventIds
	 * @param session
	 * @author zai
	 * 2020-04-12 15:54:32
	 */
	public void addSubscription(List<String> eventIds, GGXSession session) {
		if (eventIds == null || session == null) {
			return;
		}
		for (String evtid : eventIds) {
			this.addSubscription(evtid, session);
		}
	}
	
	/**
	 * 移除订阅
	 *
	 * @param eventId
	 * @param sessionGroup
	 * @author zai
	 * 2020-04-07 15:40:47
	 */
	public void removeSubscription(String eventId, GGXSession session) {
		Subscription subscription = subscriptions.get(eventId);
		if (subscription != null) {
			subscription.removeSubscription(session);
		}
	}
	
	/**
	 * 发布消息
	 *
	 * @param resp
	 * @author zai
	 * 2020-04-10 14:07:07
	 */
	public void publish(EventMessageResp resp) {
		String eventId = resp.getEventId();
		Subscription subscription = subscriptions.get(eventId);
		if (subscription != null) {
			subscription.publish(resp);
		}
	}
	
}
