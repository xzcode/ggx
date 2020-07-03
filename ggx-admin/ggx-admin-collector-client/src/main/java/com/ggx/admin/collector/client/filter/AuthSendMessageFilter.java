package com.ggx.admin.collector.client.filter;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.message.req.AuthReq;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.message.MessageData;

public class AuthSendMessageFilter implements SendMessageFilter{
	
	private GGXAdminCollectorClientConfig config;
	
	private AuthReq authReq = new AuthReq();
	
	

	public AuthSendMessageFilter(GGXAdminCollectorClientConfig config) {
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
