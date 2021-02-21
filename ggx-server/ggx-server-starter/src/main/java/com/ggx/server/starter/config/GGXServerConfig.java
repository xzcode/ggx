package com.ggx.server.starter.config;

import com.ggx.server.starter.config.module.GGXCoreServerConfigModel;
import com.ggx.server.starter.config.module.GGXEventbusServerConfigModel;
import com.ggx.server.starter.config.module.GGXGatewayConfigModel;
import com.ggx.server.starter.config.module.GGXLoadbalancerConfigModel;
import com.ggx.server.starter.config.module.GGXRegistryServerConfigModel;
import com.ggx.server.starter.config.module.GGXRoutingServerConfigModel;
import com.ggx.server.starter.config.module.GGXRpcServiceConfigModel;
import com.ggx.server.starter.config.module.GGXServiceClientConfigModel;

public class GGXServerConfig {
	
	protected boolean enabled = true;
	
	protected String[] scanPackages;
	
	protected GGXRegistryServerConfigModel registryServer;
	
	protected GGXGatewayConfigModel gateway;
	
	protected GGXCoreServerConfigModel coreServer;
	
	protected GGXLoadbalancerConfigModel loadbalancer;
	
	protected GGXRoutingServerConfigModel routingService;
	
	protected GGXServiceClientConfigModel serviceClient;
	
	protected GGXEventbusServerConfigModel eventbusServer;
	
	protected GGXRpcServiceConfigModel rpcService;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String[] getScanPackages() {
		return scanPackages;
	}

	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}

	public GGXRegistryServerConfigModel getRegistryServer() {
		return registryServer;
	}

	public void setRegistryServer(GGXRegistryServerConfigModel registryServer) {
		this.registryServer = registryServer;
	}

	public GGXGatewayConfigModel getGateway() {
		return gateway;
	}

	public void setGateway(GGXGatewayConfigModel gateway) {
		this.gateway = gateway;
	}

	public GGXCoreServerConfigModel getCoreServer() {
		return coreServer;
	}

	public void setCoreServer(GGXCoreServerConfigModel coreServer) {
		this.coreServer = coreServer;
	}

	public GGXLoadbalancerConfigModel getLoadbalancer() {
		return loadbalancer;
	}

	public void setLoadbalancer(GGXLoadbalancerConfigModel loadbalancer) {
		this.loadbalancer = loadbalancer;
	}

	public GGXServiceClientConfigModel getServiceClient() {
		return serviceClient;
	}

	public void setServiceClient(GGXServiceClientConfigModel serviceClient) {
		this.serviceClient = serviceClient;
	}

	public GGXEventbusServerConfigModel getEventbusServer() {
		return eventbusServer;
	}

	public void setEventbusServer(GGXEventbusServerConfigModel eventbusServer) {
		this.eventbusServer = eventbusServer;
	}

	public GGXRpcServiceConfigModel getRpcService() {
		return rpcService;
	}

	public void setRpcService(GGXRpcServiceConfigModel rpcService) {
		this.rpcService = rpcService;
	}

	public GGXRoutingServerConfigModel getRoutingService() {
		return routingService;
	}

	public void setRoutingService(GGXRoutingServerConfigModel routingService) {
		this.routingService = routingService;
	}

	

	
}
