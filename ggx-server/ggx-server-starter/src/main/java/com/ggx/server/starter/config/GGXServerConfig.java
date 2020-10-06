package com.ggx.server.starter.config;

import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.server.starter.constant.GGXServerMode;

public class GGXServerConfig {
	
	protected boolean enabled = true;
	
	private String[] scanPackages;
	
	protected String mode = GGXServerMode.CORE_SERVER;
	
	protected GGXServerRouterConfigModel router;
	
	protected GGXCoreServerConfig core;
	
	protected GGXServerEventbusConfigModel eventbus;
	
	protected GGXServerRegistryConfigModel registry;
	
	protected GGXServerRpcConfigModel rpc;
	
	
	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public GGXCoreServerConfig getCore() {
		return core;
	}

	public void setCore(GGXCoreServerConfig core) {
		this.core = core;
	}

	public GGXServerEventbusConfigModel getEventbus() {
		return eventbus;
	}

	public void setEventbus(GGXServerEventbusConfigModel eventbus) {
		this.eventbus = eventbus;
	}

	public GGXServerRegistryConfigModel getRegistry() {
		return registry;
	}

	public void setRegistry(GGXServerRegistryConfigModel registry) {
		this.registry = registry;
	}

	public GGXServerRouterConfigModel getRouter() {
		return router;
	}

	public void setRouter(GGXServerRouterConfigModel router) {
		this.router = router;
	}

	public GGXServerRpcConfigModel getRpc() {
		return rpc;
	}

	public void setRpc(GGXServerRpcConfigModel rpc) {
		this.rpc = rpc;
	}
	
	public String[] getScanPackages() {
		return scanPackages;
	}
	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}
}
