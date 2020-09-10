package com.ggx.router.client.service.loadblance.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.session.GGXSession;

/**
 * 路由服务组负载均衡信息
 *
 * @author zai
 * 2020-05-06 16:03:22
 */
public class RouterServiceGroupLoadblanceInfo {
	
	
	//会话与服务绑定集合
	protected final Map<GGXSession, SessionBindRouterServiceInfo> sessionBindServiceInfos = new ConcurrentHashMap<>(1000);

	/**
	 * 添加绑定信息
	 *
	 * @param session
	 * @return
	 * @author zai
	 * 2020-05-06 17:07:33
	 */
	public SessionBindRouterServiceInfo getSessionBindRouterServiceInfo(GGXSession session) {
		return sessionBindServiceInfos.get(session);
	}
	
	/**
	 * 添加如果不存在绑定信息
	 *
	 * @param key
	 * @param value 已绑定的信息
	 * @return
	 * @author zai
	 * 2020-05-06 17:07:43
	 */
	public SessionBindRouterServiceInfo putIfAbsentSessionBindRouterServiceInfo(GGXSession key, SessionBindRouterServiceInfo value) {
		return sessionBindServiceInfos.putIfAbsent(key, value);
	}
	
	/**
	 * 移除绑定信息
	 *
	 * @param session
	 * @return
	 * @author zai
	 * 2020-05-06 17:08:14
	 */
	public SessionBindRouterServiceInfo removeSessionBindRouterServiceInfo(GGXSession session) {
		return sessionBindServiceInfos.remove(session);
	}
	
}
