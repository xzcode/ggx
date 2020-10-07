package com.ggx.group.common.group.impl;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.group.common.group.GGSessionGroup;

/**
 * 抽象会话组
 *
 * @author zai 2020-04-07 14:12:29
 */
public class DefaultSessionGroup implements GGSessionGroup {

	/**
	 * 公共配置
	 */
	protected GGXCoreConfig config;
	
	/**
	 * 会话组id
	 */
	protected String groupId;
	
	/**
	 * 会话集合
	 */
	protected Map<String, GGXSession> sessionMap = new ConcurrentHashMap<String, GGXSession>();
	
	
	
	public DefaultSessionGroup(String groupId, GGXCoreConfig config) {
		this.groupId = groupId;
		this.config = config;
	}

	@Override
	public Map<String, GGXSession> getSessionMap() {
		return this.sessionMap;
	}

	public void addSession(GGXSession session) {
		if (session == null) {
			return;
		}
		this.sessionMap.put(session.getSessonId(), session);
		session.addDisconnectListener(se -> {
			se.setExpired();
			removeSession(se);
		});
	}

	public void removeSession(GGXSession session) {
		if (session == null) {
			return;
		}
		this.sessionMap.remove(session.getSessonId());
	}

	@Override
	public Charset getCharset() {
		return this.config.getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return this.config.getSerializer();
	}

	@Override
	public String getGroupId() {
		return this.groupId;
	}

}
