package com.ggx.monitor.server.handler;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.action.MessageHandler;
import com.ggx.core.common.session.GGSession;
import com.ggx.monitor.common.data.manager.MonitorDataCenter;
import com.ggx.monitor.common.data.manager.ServiceDataManager;
import com.ggx.monitor.common.data.model.service.ServiceData;
import com.ggx.monitor.common.message.req.AuthReq;
import com.ggx.monitor.common.message.req.ServiceDataReq;
import com.ggx.monitor.common.message.resp.AuthResp;
import com.ggx.monitor.server.config.GameMonitorServerConfig;
import com.ggx.monitor.server.constant.GameMonitorServerSessionKeys;

/**
 * 服务数据请求处理器
 *
 * @author zai
 * 2020-06-24 18:15:42
 */
public class ServiceDataReqHandler implements MessageHandler<ServiceDataReq>{
	
	private GameMonitorServerConfig config;
	private ServiceDataManager serviceDataManager;
	

	public ServiceDataReqHandler(GameMonitorServerConfig config) {
		this.config = config;
		MonitorDataCenter monitorDataCenter = this.config.getMonitorDataCenter();
		serviceDataManager = monitorDataCenter.getDataManager(ServiceDataManager.class);
	}
	


	@Override
	public void handle(MessageData<ServiceDataReq> messageData) {
		GGSession session = messageData.getSession();
		
		ServiceDataReq req = messageData.getMessage();
		ServiceData serviceData = req.getServiceData();
		serviceDataManager.put(serviceData.getServiceId(), serviceData);
		
	}


	

}
