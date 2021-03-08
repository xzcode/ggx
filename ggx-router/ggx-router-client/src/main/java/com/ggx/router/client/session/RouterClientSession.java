package com.ggx.router.client.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.VirtualSession;
import com.ggx.router.common.message.req.RouteMessageReq;

public class RouterClientSession extends VirtualSession {


	public RouterClientSession(String sessionId, GGXSession realSession, GGXCoreConfig config) {
		super(sessionId, realSession, config);
	}

	@Override
	public GGXFuture<?> send(Pack pack) {
		RouteMessageReq req = new RouteMessageReq();
		req.setAction(pack.getAction());
		req.setMessage(pack.getMessage());
		req.setRequestSeq(pack.getRequestSeq());
		req.setTranferSessionId(pack.getSession().getSessionId());
		
		return realSession.send(req);
	}
	
	
	

}
