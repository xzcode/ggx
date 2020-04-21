package com.ggx.core.client.starter;

import com.ggx.core.client.config.GGClientConfig;
import com.ggx.core.common.future.IGGFuture;
import com.ggx.core.common.session.GGSession;

public interface IGGClientStarter {
	
	IGGFuture connect(String host, int port);
	
	IGGFuture disconnect(GGSession session);
	
	void setConfig(GGClientConfig config);
	
	void shutdown();
}
