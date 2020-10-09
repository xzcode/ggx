package com.ggx.core.server.config;

import java.util.concurrent.ThreadFactory;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.message.pingpong.GGXPingPongServerEventListener;
import com.ggx.core.common.message.pingpong.GGXPingPongController;
import com.ggx.core.server.port.PortChangeStrategy;

import io.netty.channel.nio.NioEventLoopGroup;

public class GGXCoreServerConfig extends GGXCoreConfig{
	
	protected String	serverName = "GGXServer";
	
	protected String	host = "0.0.0.0";
	
	//当前端口
	protected int 		port = 9999;
	
	
	
	//最小随机端口
	protected int 		minRandomPort = 20000;
	
	//最大随机端口
	protected int 		maxRandomPort = 60000;
	
	//使用随机端口启动
	protected boolean 	bootWithRandomPort = false;
	
	//如果端口被占用是否更换端口并重新启动
	protected boolean 	changeAndRebootIfPortInUse = true;
	
	//端口更改策略
	protected String 	portChangeStrategy = PortChangeStrategy.RANDOM;
	
	protected NioEventLoopGroup bossGroup;
	
	protected ThreadFactory bossGroupThreadFactory;
	
	
	@Override
	public void init() {
		if (super.inited) {
			return;
		}
		
		if (bossGroupThreadFactory == null) {
			bossGroupThreadFactory = new GGXThreadFactory("gg-boss-", false);
		}
		
		if (bossGroup == null) {
			bossGroup = new NioEventLoopGroup(getBossThreadSize(),bossGroupThreadFactory);				
		}
		
		super.init();
		

		if (isPingPongEnabled()) {
			messageControllerManager.register(new GGXPingPongController(this));	
			eventManager.addEventListener(GGXCoreEvents.Idle.READ, new GGXPingPongServerEventListener(this));
		}
		
	}
	public NioEventLoopGroup getBossGroup() {
		return bossGroup;
	}
	public void setBossGroup(NioEventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}
	public ThreadFactory getBossGroupThreadFactory() {
		return bossGroupThreadFactory;
	}
	public void setBossGroupThreadFactory(ThreadFactory bossGroupThreadFactory) {
		this.bossGroupThreadFactory = bossGroupThreadFactory;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getPortChangeStrategy() {
		return portChangeStrategy;
	}
	public void setPortChangeStrategy(String portChangeStrategy) {
		this.portChangeStrategy = portChangeStrategy;
	}
	public int getMinRandomPort() {
		return minRandomPort;
	}
	public void setMinRandomPort(int minRandomPort) {
		this.minRandomPort = minRandomPort;
	}
	public int getMaxRandomPort() {
		return maxRandomPort;
	}
	public void setMaxRandomPort(int maxRandomPort) {
		this.maxRandomPort = maxRandomPort;
	}
	public boolean isChangeAndRebootIfPortInUse() {
		return changeAndRebootIfPortInUse;
	}
	public void setChangeAndRebootIfPortInUse(boolean retryRandomPortIfPortInUse) {
		this.changeAndRebootIfPortInUse = retryRandomPortIfPortInUse;
	}
	public boolean isBootWithRandomPort() {
		return bootWithRandomPort;
	}
	public void setBootWithRandomPort(boolean bootWithRandomPort) {
		this.bootWithRandomPort = bootWithRandomPort;
	}
	
	
	
}
