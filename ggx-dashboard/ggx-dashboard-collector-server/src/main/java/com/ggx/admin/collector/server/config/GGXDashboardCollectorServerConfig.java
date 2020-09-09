package com.ggx.admin.collector.server.config;

import com.ggx.core.common.executor.SingleThreadTaskExecutor;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.eventbus.client.EventbusClient;
import com.ggx.registry.client.RegistryClient;

/**
 * 配置
 * 
 * 
 * @author zai
 * 2019-10-04 17:23:47
 */
public class GGXDashboardCollectorServerConfig {

	private TaskExecutor taskExecutor = new SingleThreadTaskExecutor();
	
	private RegistryClient registryClient;
	
	private EventbusClient eventbusClient;
	
	
	public String getServiceId() {
		return getRegistryClient().getServiceId();
	}

	public RegistryClient getRegistryClient() {
		return registryClient;
	}

	public void setRegistryClient(RegistryClient registryClient) {
		this.registryClient = registryClient;
	}
	
	public EventbusClient getEventbusClient() {
		return eventbusClient;
	}
	
	public void setEventbusClient(EventbusClient eventbusClient) {
		this.eventbusClient = eventbusClient;
	}
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	
	
}
