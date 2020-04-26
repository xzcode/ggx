package com.xzcode.ggcloud.eventbus.server.subscription;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.common.message.resp.EventMessageResp;
import com.ggx.core.common.session.GGSession;
import com.xzcode.ggcloud.eventbus.server.config.EventbusServerConfig;
import com.xzcode.ggcloud.session.group.common.group.GGSessionGroup;
import com.xzcode.ggcloud.session.group.common.group.impl.DefaultSessionGroup;

/***
 * 订阅信息
 *
 * @author zai
 * 2020-04-07 11:26:42
 */
public class Subscription {
	
	
	private EventbusServerConfig config;
	
	//事件id
	private String eventId;
	
	//订阅的会话集合
	private Map<String, GGSessionGroup> sessionGroups = new ConcurrentHashMap<>();
	
	
	public Subscription(String eventId, EventbusServerConfig config) {
		super();
		this.eventId = eventId;
		this.config = config;
	}
	
	public void publish(EventMessageResp resp) {
		for (Entry<String, GGSessionGroup> entry : sessionGroups.entrySet()) {
			GGSessionGroup group = entry.getValue();
			if (group != null) {
				group.sendToRandomOne(resp);
			}
		}
	}

	public void addSubscription(GGSession session) {
		GGSessionGroup sessionGroup = sessionGroups.get(session.getGroupId());
		if (sessionGroup == null) {
			sessionGroup = new DefaultSessionGroup(session.getGroupId(), config.getSessionGroupServer().getConfig().getSessionServer().getConfig());
			GGSessionGroup putIfAbsent = this.sessionGroups.putIfAbsent(session.getGroupId(), sessionGroup);
			if (putIfAbsent != null) {
				sessionGroup = putIfAbsent;
			}
		}
		sessionGroup.addSession(session);
		session.addDisconnectListener(se -> {
			removeSubscription(se);
		});
		
		
	}
	
	public void removeSubscription(GGSession session) {
		GGSessionGroup group = this.sessionGroups.get(session.getGroupId());
		if (group != null) {
			group.removeSession(session);
		}
	}
	
	public String getEventId() {
		return eventId;
	}
	
}
