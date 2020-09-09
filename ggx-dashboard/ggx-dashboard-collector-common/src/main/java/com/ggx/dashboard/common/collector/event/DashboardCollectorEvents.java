package com.ggx.dashboard.common.collector.event;

/**
 * 仪表盘数据收集器相关事件id常量
 *
 * @author zai
 * 2020-09-09 15:29:46
 */
public interface DashboardCollectorEvents {
	
	/**
	 * 事件id前缀
	 */
	String EVENT_ID_PREFIX = "GGX.DASHBOARD.COLLECTOR.";
	
	/**
	 * 请求服务数据事件id
	 */
	String REQUEST_SERVICE_DATA = EVENT_ID_PREFIX + "REQUEST.SERVICE.DATA";
	
	/**
	 * 发送服务数据事件id
	 */
	String SEND_SERVICE_DATA = EVENT_ID_PREFIX + "PUBLISH.SERVICE.DATA";
	
}
