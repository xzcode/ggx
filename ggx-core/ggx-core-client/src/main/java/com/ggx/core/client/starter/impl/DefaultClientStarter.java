package com.ggx.core.client.starter.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.client.config.GGXCoreClientConfig;
import com.ggx.core.client.starter.GGClientStarter;
import com.ggx.core.common.constant.ProtocolTypeConstants;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.future.GGNettyFuture;
import com.ggx.core.common.handler.MixedSocketChannelInitializer;
import com.ggx.core.common.handler.TcpChannelInitializer;
import com.ggx.core.common.handler.WebSocketChannelInitializer;
import com.ggx.core.common.session.GGSession;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;

public class DefaultClientStarter implements GGClientStarter {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultClientStarter.class);
	
	private GGXCoreClientConfig config;
	
	
	
    public DefaultClientStarter(GGXCoreClientConfig config) {
    	this.config = config;
    	init();
    }
    
    public void init() {
    	
    	Bootstrap boot = config.getBootstrap();

        //设置工作线程组
        boot.group(config.getWorkerGroup());
        
        if (logger.isDebugEnabled()) {
        	boot.handler(new LoggingHandler(LogLevel.INFO));				
		}else {
			boot.handler(new LoggingHandler(LogLevel.WARN));
		}
        
        //设置channel类型
        boot.channel(NioSocketChannel.class); 
        
        //设置消息处理器
        if (config.getProtocolType().equals(ProtocolTypeConstants.MIXED)) {
        	boot.handler(new MixedSocketChannelInitializer(config));
		}
        else if (config.getProtocolType().equals(ProtocolTypeConstants.TCP)) {
        	boot.handler(new TcpChannelInitializer(config));
        }
        else if (config.getProtocolType().equals(ProtocolTypeConstants.WEBSOCKET)) {
        	boot.handler(new WebSocketChannelInitializer(config));
        }else {
			throw new RuntimeException("ChannelInitializer Not Set!");
		}
        
        boot.option(ChannelOption.TCP_NODELAY, true);  
        
      
	}
    
    
    public GGFuture connect(String host, int port) {
        try {
        	Bootstrap boot = config.getBootstrap();
            // 连接服务器
            ChannelFuture future = boot.connect(host, port);
            return new GGNettyFuture(future);
        }catch (Exception e) {
        	throw new RuntimeException("GGClient connect error !! ", e);
		}
        
    }
    

	@Override
	public GGFuture disconnect(GGSession session) {
		return session.disconnect();
	}
	
	public GGFuture shutdown() {
		GGNettyFuture ggFuture = new GGNettyFuture();
		try {
			Future<?> future = config.getWorkerGroup().shutdownGracefully();
			ggFuture.setFuture(future);
		} catch (Exception e) {
			logger.error("Shutdown error!", e);
		}
		return ggFuture;
	}
    
    public void setConfig(GGXCoreClientConfig config) {
		this.config = config;
	}
    public GGXCoreClientConfig getConfig() {
		return config;
	}

    
}
