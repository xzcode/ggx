package com.ggx.dashboard.client.filter;

import com.ggx.admin.common.collector.message.req.AuthReq;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.message.MessageData;
import com.ggx.dashboard.client.config.GGXDashboardClientConfig;

public class AuthSendMessageFilter implements SendMessageFilter{
	
	private GGXDashboardClientConfig config;
	
	private AuthReq authReq = new AuthReq();
	
	

	public AuthSendMessageFilter(GGXDashboardClientConfig config) {
		super();
		this.config = config;
	}



	@Override
	public boolean doFilter(MessageData<?> data) {
		if (data.getAction().equals(authReq.getActionId())) {
			return true;
		}
		if (this.config.isAuthed()) {
			return true;
		}
		return false;
	}

}
