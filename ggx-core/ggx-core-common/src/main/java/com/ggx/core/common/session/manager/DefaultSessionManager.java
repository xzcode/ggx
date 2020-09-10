package com.ggx.core.common.session.manager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.session.GGXSession;

/**
 * session管理器
 * 
 * 
 * @author zai
 * 2019-10-02 23:17:21
 */
public class DefaultSessionManager implements SessionManager {
	
	private GGXCoreConfig config;
	
	private final Map<String, GGXSession> sessionMap = new ConcurrentHashMap<>(1000);
	
	public DefaultSessionManager(GGXCoreConfig config) {
		this.config = config;
		
		this.startSessionExpireCheckTask();
	}
	
	
	/**
	 * 启动检查session过期任务
	 *
	 * @author zai
	 * 2020-04-13 10:25:24
	 */
	protected void startSessionExpireCheckTask() {
		TaskExecutor taskExecutor = this.config.getTaskExecutor();
		taskExecutor.scheduleWithFixedDelay(10L * 1000L, this.config.getSessionExpireMs(), TimeUnit.MILLISECONDS, () -> {
			for (Entry<String, GGXSession> entry : sessionMap.entrySet()) {
				GGXSession session = entry.getValue();
				session.checkExpire();
				if (session.isExpired()) {
					session.disconnect();
				}
			}
		});
	}
	
	
	@Override
	public GGXSession addSessionIfAbsent(GGXSession session) {
		GGXSession putIfAbsent = sessionMap.putIfAbsent(session.getSessonId(), session);
		if (putIfAbsent == null) {
			//添加断开监听
			session.addDisconnectListener( s -> {
				//断开连接从管理器中移除session
				remove(s.getSessonId());
				
			});
		}
		return putIfAbsent;
	}
	
	@Override
	public GGXSession getSession(String sessionId) {
		if (sessionId != null) {
			GGXSession session = sessionMap.get(sessionId);
			if (session != null) {
				session.updateExpire();
			}
			return session;
		}
		return null;
	}
	
	@Override
	public GGXSession remove(String sessionId) {
		if (sessionId != null) {
			return sessionMap.remove(sessionId);
		}
		return null;
	}

	@Override
	public void eachSession(EachData<GGXSession> eachData) {
		
		for (Entry<String, GGXSession> entry : sessionMap.entrySet()) {
			if (!eachData.each(entry.getValue())) {
				break;
			}
		}
	}

	@Override
	public void disconnectAllSession() {
		if (sessionMap != null) {
			eachSession(session -> {
				session.disconnect();
				return true;
			});
		}
	}


	@Override
	public GGXSession randomGetSession() {
		Set<Entry<String, GGXSession>> entrySet = sessionMap.entrySet();
		if (entrySet.size() == 0) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Entry<String, GGXSession> entry = (Entry<String, GGXSession>) entrySet.toArray()[ThreadLocalRandom.current().nextInt(entrySet.size())];
		return entry.getValue();
	}

}
