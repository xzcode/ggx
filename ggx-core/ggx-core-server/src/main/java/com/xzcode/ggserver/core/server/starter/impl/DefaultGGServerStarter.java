package com.xzcode.ggserver.core.server.starter.impl;


import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.future.GGNettyFuture;
import com.ggx.core.common.handler.MixedSocketChannelInitializer;
import com.ggx.core.common.handler.TcpChannelInitializer;
import com.ggx.core.common.handler.WebSocketChannelInitializer;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.xzcode.ggserver.core.server.config.GGServerConfig;
import com.xzcode.ggserver.core.server.port.PortChangeStrategy;
import com.xzcode.ggserver.core.server.starter.GGServerStarter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;

/**
 * socket 服务启动器
 *
 * @author zai
 * 2018-12-20 10:17:44
 */
public class DefaultGGServerStarter implements GGServerStarter {
	
	protected static final Logger logger = LoggerFactory.getLogger(DefaultGGServerStarter.class);
	
	protected GGServerConfig config;
	
	protected  Channel serverChannel;
	
    public DefaultGGServerStarter(GGServerConfig config) {
    	
    	this.config = config;
    }
    
    public GGFuture start() {
    	
        try {
        	
        	
        	
            ServerBootstrap boot = new ServerBootstrap(); 
            
            //设置工作线程组
            boot.group(config.getBossGroup(), config.getWorkerGroup());
            
            if (logger.isDebugEnabled()) {
            	boot.handler(new LoggingHandler(LogLevel.INFO));				
			}else {
				boot.handler(new LoggingHandler(LogLevel.WARN));
			}
            
            //设置channel类型
            boot.channel(NioServerSocketChannel.class);
            
            //设置消息处理器
            if (config.getProtocolType().equals(ProtocolTypeConstants.MIXED)) {
            	boot.childHandler(new MixedSocketChannelInitializer(config));
    		}
            else if (config.getProtocolType().equals(ProtocolTypeConstants.TCP)) {
            	boot.childHandler(new TcpChannelInitializer(config));
            }
            else if (config.getProtocolType().equals(ProtocolTypeConstants.WEBSOCKET)) {
            	boot.childHandler(new WebSocketChannelInitializer(config));
            }
            
            boot.option(ChannelOption.SO_BACKLOG, config.getSoBacklog()); 
            boot.option(ChannelOption.SO_REUSEADDR, true); 
            
            boot.childOption(ChannelOption.SO_REUSEADDR, config.isSoReuseaddr()); 
            boot.childOption(ChannelOption.TCP_NODELAY, true); 
            
            boot.childOption(ChannelOption.SO_KEEPALIVE, true); 
            
            if (this.config.isBootWithRandomPort()) {
				this.config.setPort(getRandomPort());
			}
    
            ChannelFuture future = boot.bind(this.config.getPort());
            GGNettyFuture ggFuture = new GGNettyFuture();
            
            future.addListener((f) -> {
            	handleStartFutureCallback(boot, ggFuture, f);
            });
            
            return ggFuture;
            
        }catch (Exception e) {
        	shutdown();
        	throw new RuntimeException("GGServer start failed !! ", e);
        }
    }
    
    private void handleStartFutureCallback(ServerBootstrap boot, GGNettyFuture ggFuture, Future<? super Void> f) {
    	String logoString = getLogoString();
    	if (f.isSuccess()) {
    		GGLoggerUtil.getLogger().warn("{}\n{} started successfully on port {}!\n", logoString, config.getServerName(), config.getPort());
    		ggFuture.setFuture(f);
		}else {
			int oldPort = this.config.getPort();
			if (this.config.isChangeAndRebootIfPortInUse()) {
				//如果端口被占用，重新获取新端口并启动
				if (f.cause() instanceof java.net.BindException) {
					int newPort = this.config.getPort();
					if (this.config.getPortChangeStrategy().contentEquals(PortChangeStrategy.RANDOM)) {
						newPort = getRandomPort();
						this.config.setPort(newPort);
					}else if (this.config.getPortChangeStrategy().contentEquals(PortChangeStrategy.INCREMENT)) {
						newPort += 1;
					}
					this.config.setPort(newPort);
					GGLoggerUtil.getLogger(this).warn("Port [{}] is already in use, try to change another port [{}] and restart!", oldPort, newPort );
					
					boot.bind(config.getPort()).addListener((f2) -> {
		            	handleStartFutureCallback(boot, ggFuture, f2);
		            });
				}
				return;
			}
			
			GGLoggerUtil.getLogger().warn("{}\n{} FAILED to start on port {}!\nError:{}\n", logoString, config.getServerName(), config.getPort(),f.cause());
			
			shutdown();
		}
    }
    
    /**
     * 获取随机端口
     *
     * @return
     * @author zai
     * 2020-05-18 11:42:27
     */
    private int getRandomPort() {
    	return ThreadLocalRandom.current().nextInt(this.config.getMinRandomPort(), this.config.getMaxRandomPort());
    }
    
    private String getLogoString() {
    	StringBuilder sb = new StringBuilder(512);
    	try (
    			InputStream is = this.getClass().getResourceAsStream("/ggserver-logo.txt");
			){
    		byte[] buff = new byte[1024];
    		int len = -1;
    		while ((len = is.read(buff)) != -1) {
				String logoString = new String(buff, 0, len, config.getCharset());
				sb.append(logoString);
			}
		} catch (Exception e) {
			logger.error("GGServer print logo Error!", e);
		}
    	return sb.toString();
    }
    
    /**
     * 关闭socket server
     * 
     * 
     * @author zai
     * 2017-07-27
     */
    public void shutdown() {
    	if (config.getBossGroup() != null) {
    		config.getBossGroup().shutdownGracefully();			
		}
    	if (config.getWorkerGroup() != null) {
    		config.getWorkerGroup().shutdownGracefully();		
		}
	}
    
    public void setConfig(GGServerConfig config) {
		this.config = config;
	}
    public GGServerConfig getConfig() {
		return config;
	}
    
}
