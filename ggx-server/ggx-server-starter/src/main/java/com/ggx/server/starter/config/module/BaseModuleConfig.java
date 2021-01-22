package com.ggx.server.starter.config.module;

import com.ggx.core.server.config.GGXCoreServerConfig;
import com.ggx.server.starter.config.sub.GGXServerEventbusConfigSubModel;
import com.ggx.server.starter.config.sub.GGXServerRegistryConfigSubModel;
import com.ggx.server.starter.config.sub.GGXServerRouterConfigSubModel;
import com.ggx.server.starter.config.sub.GGXServerRpcConfigSubModel;

public abstract class BaseModuleConfig {

	protected boolean enabled;

	protected GGXServerRouterConfigSubModel router;

	protected GGXCoreServerConfig core;

	protected GGXServerEventbusConfigSubModel eventbus;

	protected GGXServerRegistryConfigSubModel registry;

	protected GGXServerRpcConfigSubModel rpc;

	public GGXServerRouterConfigSubModel getRouter() {
		return router;
	}

	public void setRouter(GGXServerRouterConfigSubModel router) {
		this.router = router;
	}

	public GGXCoreServerConfig getCore() {
		return core;
	}

	public void setCore(GGXCoreServerConfig core) {
		this.core = core;
	}

	public GGXServerEventbusConfigSubModel getEventbus() {
		return eventbus;
	}

	public void setEventbus(GGXServerEventbusConfigSubModel eventbus) {
		this.eventbus = eventbus;
	}

	public GGXServerRegistryConfigSubModel getRegistry() {
		return registry;
	}

	public void setRegistry(GGXServerRegistryConfigSubModel registry) {
		this.registry = registry;
	}

	public GGXServerRpcConfigSubModel getRpc() {
		return rpc;
	}

	public void setRpc(GGXServerRpcConfigSubModel rpc) {
		this.rpc = rpc;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
