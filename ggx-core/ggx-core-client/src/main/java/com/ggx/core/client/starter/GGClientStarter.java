package com.ggx.core.client.starter;

import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.session.GGSession;

public interface GGClientStarter {
	
	GGFuture connect(String host, int port);
	
	GGFuture disconnect(GGSession session);
	
	void setConfig(GGClientConfig config);
	
	GGFuture shutdown();
}
