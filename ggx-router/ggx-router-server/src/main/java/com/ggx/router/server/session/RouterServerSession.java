package com.ggx.router.server.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.impl.VirtualSession;
import com.ggx.router.common.message.resp.RouteMessageResp;

public class RouterServerSession extends VirtualSession {

	
	
	public RouterServerSession(String sessionId, GGXSession realSession, GGXCoreConfig config) {
		super(sessionId, realSession, config);
	}
	

	@Override
	public GGXFuture<?> send(Pack pack) {
		RouteMessageResp resp = new RouteMessageResp();
		resp.setAction(pack.getAction());
		resp.setMessage(pack.getMessage());
		resp.setTranferSessionId(pack.getSession().getSessionId());
		resp.setRequestSeq(pack.getRequestSeq());
		
		return realSession.send(resp);
	}
	
	
	

}
