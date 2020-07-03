package com.ggx.admin.common.collector.message.req;

import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 服务器数据更新请求
 *
 * @author zai
 * 2020-06-24 16:47:34
 */
public class ServerDataReq  extends AbstractMessage {

	// 服务信息
	private ServerData serverData;

	public ServerDataReq() {

	}

	public ServerDataReq(ServerData serverData) {
		this.serverData = serverData;
	}

	public ServerData getServerData() {
		return serverData;
	}

	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	
	

}
