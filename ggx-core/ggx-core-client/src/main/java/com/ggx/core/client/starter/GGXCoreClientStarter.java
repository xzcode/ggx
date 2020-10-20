package com.ggx.core.client.starter;

import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.session.GGXSession;

public interface GGXCoreClientStarter {
	
	GGXFuture<?> connect(String host, int port);
	
	GGXFuture<?> disconnect(GGXSession session);
	
	void setConfig(GGXCoreClientConfig config);
	
	GGXFuture<?> shutdown();
}
