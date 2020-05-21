package com.ggx.group.common.group.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGSession;
import com.ggx.group.common.group.GGSessionGroup;
import com.ggx.group.common.group.impl.DefaultSessionGroup;

/**
 * 会话组管理器
 *
 * @author zai
 * 2020-04-07 15:11:36
 */
public class GGSessionGroupManager {
	
	/**
	 * 公共配置
	 */
	private GGConfig config;
	/**
	 * 会话组集合
	 */
	protected Map<String, GGSessionGroup> sessionGroupMap = new ConcurrentHashMap<String, GGSessionGroup>();
	
	
	public GGSessionGroupManager(GGConfig config) {
		super();
		this.config = config;
	}

	/**
	 * 添加会话
	 *
	 * @param sessionGroupId 组id
	 * @param session 会话
	 * @author zai
	 * 2020-04-07 15:11:47
	 */
	public void addSession(String sessionGroupId, GGSession session) {
		
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(sessionGroupId);
		if (sessionGroup == null) {
			sessionGroup = new DefaultSessionGroup(sessionGroupId, this.config);
			GGSessionGroup putIfAbsent = sessionGroupMap.putIfAbsent(sessionGroupId, sessionGroup);
			if (putIfAbsent != null) {
				sessionGroup = putIfAbsent;
			}
		}
		if (session == null) {
			return;
		}
		sessionGroup.addSession(session);
	}

	/**
	 * 移除会话
	 *
	 * @param sessionGroupId
	 * @param session
	 * @author zai
	 * 2020-04-07 15:12:00
	 */
	public void removeSession(String sessionGroupId, GGSession session) {
		if (session == null) {
			return;
		}
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(sessionGroupId);
		if (sessionGroup != null) {
			sessionGroup.removeSession(session);
		}
	}
	
	/**
	 * 发送给指定组内的所有session
	 *
	 * @param groupId
	 * @param pack
	 * @return
	 * @author zai
	 * 2020-04-08 16:23:40
	 */
	public GGFuture sendToAll(String groupId, Pack pack) {
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(groupId);
		if (sessionGroup != null) {
			return sessionGroup.sendToAll(pack);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	

	/**
	 * 随机发送到一个会话中
	 *
	 * @param pack
	 * @return
	 * @author zai 2020-04-07 14:49:06
	 */
	public GGFuture sendToRandomOne(String groupId, Pack pack) {
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(groupId);
		if (sessionGroup != null) {
			return sessionGroup.sendToRandomOne(pack);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	
	
	public GGSession getRandomOne(String groupId) {
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(groupId);
		if (sessionGroup != null) {
			return sessionGroup.getRandomOne();
		}
		return null;
	}
	
	/**
	 * 随机发送到一个会话中
	 *
	 * @param message
	 * @return
	 * @author zai
	 * 2020-04-07 15:38:44
	 */
	public GGFuture sendToRandomOne(String groupId, Message message) {
		GGSessionGroup sessionGroup = this.sessionGroupMap.get(groupId);
		if (sessionGroup != null) {
			return sessionGroup.sendToRandomOne(message);
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}

}
