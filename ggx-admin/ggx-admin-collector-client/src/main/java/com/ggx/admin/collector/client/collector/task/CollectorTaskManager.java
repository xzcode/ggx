package com.ggx.admin.collector.client.collector.task;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.data.collector.DataCollector;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.future.GGFuture;

public class CollectorTaskManager {
	
	private GGXAdminCollectorClientConfig config;

	protected Map<Class<?>, DataCollector> collectors = new ConcurrentHashMap<Class<?>, DataCollector>();
	
	protected Map<Class<?>, GGFuture> taskFutures = new ConcurrentHashMap<Class<?>, GGFuture>();

	
	public CollectorTaskManager(GGXAdminCollectorClientConfig config) {
		this.config = config;
	}
	
	public void addCollector(DataCollector collector) {
		this.collectors.put(collector.getClass(), collector);
	}
	
	public void startAllTask() {
		TaskExecutor taskExecutor = config.getTaskExecutor();
		for (Entry<Class<?>, DataCollector> entry : collectors.entrySet()) {
			DataCollector collector = entry.getValue();
			GGFuture future = taskExecutor.scheduleWithFixedDelay(collector.collectInitDelayMs(), collector.collectPeriodMs(), TimeUnit.MILLISECONDS, collector);
			this.taskFutures.put(collector.getClass(), future);
		}
	}
	
	public void cleanAllTasks() {
		for (Entry<Class<?>, GGFuture> entry : taskFutures.entrySet()) {
			entry.getValue().cancel();
		}
	}

}
