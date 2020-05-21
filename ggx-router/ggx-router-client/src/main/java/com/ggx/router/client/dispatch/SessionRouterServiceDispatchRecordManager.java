package com.ggx.router.client.dispatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.core.common.session.GGSession;
import com.ggx.router.client.service.RouterService;

/**
 * 路由服务转发记录管理器
 *
 * @author zai
 * 2020-05-20 12:05:25
 */
public class SessionRouterServiceDispatchRecordManager {
	
	/**
	 * 记录集合Map<会话id, 记录>
	 */
	protected final Map<GGSession, SessionRouterServiceDispatchRecord> recordMap = new ConcurrentHashMap<>(1000);
	
	/**
	 * 添加服务
	 *
	 * @param service
	 * @author zai
	 * 2020-05-03 13:31:57
	 */
	public void addService(GGSession session, RouterService service) {
		
		SessionRouterServiceDispatchRecord record = this.recordMap.get(session);
		if (record == null) {
			record = new SessionRouterServiceDispatchRecord(session.getSessonId());
			SessionRouterServiceDispatchRecord putIfAbsent = this.recordMap.putIfAbsent(session, record);
			if (putIfAbsent != null) {
				record = putIfAbsent;
			}else {
				session.addDisconnectListener(session2 -> {
					this.recordMap.remove(session);
				});
			}
		}
		if (record.getService(service.getServiceId()) == service) {
			return;
		}
		record.addServiceIfAbsent(service);
		
	}
	
	/**
	 * 获取记录
	 *
	 * @param serviceGroupId
	 * @return
	 * @author zai
	 * 2020-05-03 13:47:11
	 */
	public SessionRouterServiceDispatchRecord getRecord(GGSession session) {
		return this.recordMap.get(session);
	}

}
