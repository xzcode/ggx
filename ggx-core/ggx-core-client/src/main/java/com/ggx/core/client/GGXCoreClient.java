package com.ggx.core.client;

import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.client.starter.GGXCoreClientStarter;
import com.ggx.core.client.starter.impl.DefaultGGXCoreClientStarter;
import com.ggx.core.common.config.GGXCoreConfigSupport;
import com.ggx.core.common.control.GGXSessionContolSupport;
import com.ggx.core.common.future.GGXFuture;

/**
 * 客户端
 * 
 * @author zzz
 * 2019-09-24 17:19:54
 */
public class GGXCoreClient 
implements 
	GGXCoreConfigSupport<GGXCoreClientConfig>,
	GGXSessionContolSupport
{
	
	private GGXCoreClientConfig config;
	
	private GGXCoreClientStarter clientStarter;
	
	public GGXFuture connect(String host, int port) {
		return clientStarter.connect(host, port);
	}
	

	public GGXCoreClient(GGXCoreClientConfig config) {
		this.config = config;
		if (!this.config.isInited()) {
			this.config.init();
		}
		this.clientStarter = new DefaultGGXCoreClientStarter(config);
	}
	
	@Override
	public GGXCoreClientConfig getConfig() {
		return config;
	}
	
	public GGXFuture shutdown() {
		return clientStarter.shutdown();
	}

}
