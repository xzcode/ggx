package com.ggx.router.server.session;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.VirtualSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.router.common.message.resp.RouteMessageResp;

public class RouterServerSession extends VirtualSession {

	

	public RouterServerSession(String sessionId, GGXSession realSession, SessionManager realSessionManager, GGXCoreConfig config) {
		super(sessionId, realSession, realSessionManager, config);
	}

	@Override
	public GGXFuture<?> send(Pack pack) {
		RouteMessageResp resp = new RouteMessageResp();
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setTranferSessionId(pack.getSession().getSessionId());
		
		if (this.realSession == null || this.realSession.isExpired()) {
			this.realSession = realSessionManager.getRandomSession();
		}
		if (this.realSession == null) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		return realSession.send(resp);
	}
	
	
	

}
