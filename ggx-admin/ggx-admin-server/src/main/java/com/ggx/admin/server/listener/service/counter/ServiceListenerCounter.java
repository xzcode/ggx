package com.ggx.admin.server.listener.service.counter;

import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.common.session.GGSession;

/**
 * 服务监听器计数器
 *
 * @author zai
 * 2020-07-20 18:38:43
 */
public class ServiceListenerCounter {
	
	protected String serviceId;
	
	protected GGSession session;
	
	protected AtomicInteger count = new AtomicInteger(0);
	

	public ServiceListenerCounter(String serviceId, GGSession session) {
		this.serviceId = serviceId;
		this.session = session;
	}
	
	/**
	 * 增加计数
	 *
	 * @return
	 * @author zai
	 * 2020-07-20 18:38:33
	 */
	public int increCounter() {
		return count.incrementAndGet();
	}

	public GGSession getSession() {
		return session;
	}

	public void setSession(GGSession session) {
		this.session = session;
	}

	public AtomicInteger getCount() {
		return count;
	}

	public void setCount(AtomicInteger count) {
		this.count = count;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
	
	

}
