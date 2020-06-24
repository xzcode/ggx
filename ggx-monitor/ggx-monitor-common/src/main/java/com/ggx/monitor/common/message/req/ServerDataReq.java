package com.ggx.monitor.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.monitor.common.constant.GameMonitorConstant;
import com.ggx.monitor.common.data.model.server.ServerData;

/**
 * 服务器数据更新请求
 *
 * @author zai
 * 2020-06-24 16:47:34
 */
public class ServerDataReq implements Message {

	public static final String ACTION = GameMonitorConstant.ACTION_ID_PREFIX + "SERVER.DATA.REQ";

	@Override
	public String getActionId() {
		return ACTION;
	}
	
	//服务id
	private String serviceId;

	// 服务信息
	private ServerData serverData;

	public ServerDataReq() {

	}

	public ServerDataReq(String serviceId, ServerData serverData) {
		super();
		this.serviceId = serviceId;
		this.serverData = serverData;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public ServerData getServerData() {
		return serverData;
	}

	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	
	

}
