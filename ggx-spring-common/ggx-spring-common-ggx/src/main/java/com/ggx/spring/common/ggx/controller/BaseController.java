package com.ggx.spring.common.ggx.controller;

import com.ggx.core.common.session.GGXSession;
import com.ggx.spring.common.ggx.session.BaseSessionKeys;
import com.ggx.spring.common.ggx.session.UserSessionInfo;

public class BaseController {
	
	public UserSessionInfo getUserSessionInfo(GGXSession session) {
		return session.getAttribute(BaseSessionKeys.USER_SESSION_INFO, UserSessionInfo.class);
	}
	public void setUserSessionInfo(GGXSession session, String userId, String token) {
		session.addAttribute(BaseSessionKeys.USER_SESSION_INFO, new UserSessionInfo(userId, token, session.getSessionId()));
	}

}
