package com.ggx.admin.collector.server.filter;

import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerSessionKeys;
import com.ggx.admin.common.collector.message.req.AuthReq;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.core.spring.support.annotation.GGXFilter;

@GGXFilter
public class AuthFilter implements ReceiveMessageFilter{
	
	private AuthReq req = new AuthReq();

	@Override
	public boolean doFilter(MessageData<?> messageData) {
		if (messageData.getAction().equals(req.getActionId())) {
			return true;
		}
		
		GGSession session = messageData.getSession();
		Boolean auth = session.getAttribute(GGXAdminCollectorServerSessionKeys.IS_AUTHED, Boolean.class);
		if (auth != null && auth) {
			return true;
		}
		GGLoggerUtil.getLogger(this).warn("Session Not Authed!");
		return false;
	}

}
