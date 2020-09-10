package com.ggx.dashboard.server.model;

import com.ggx.core.common.session.GGXSession;

/**
 * 会话监听信息
 *
 * @author zai 2020-07-17 16:16:37
 */
public class ServiceInfoSessionListener {

	/**
	 * 超时延迟（毫秒）
	 */
	public static final long TIMEOUT_DELAY_MS = 60L * 1000L;

	// 会话对象
	private GGXSession session;
	
	// 服务id
	private String serviceId;

	// 最后更新时间
	private long lastUpdateTime = System.currentTimeMillis();
	

	public ServiceInfoSessionListener(GGXSession session, String serviceId) {
		this.session = session;
		this.serviceId = serviceId;
	}

	/**
	 * 是否超时
	 *
	 * @return
	 * @author zai
	 * 2020-07-17 16:20:33
	 */
	public boolean isTimeout() {
		return System.currentTimeMillis() - this.lastUpdateTime > TIMEOUT_DELAY_MS;
	}
	
	/**
	 * 刷新最后更新时间
	 *
	 * @author zai
	 * 2020-07-17 16:23:45
	 */
	public void refreshLastUpdateTime() {
		this.lastUpdateTime = System.currentTimeMillis(); 
	}

	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	

}
