package com.ggx.core.server;

import com.ggx.core.common.config.GGXCoreConfigSupport;
import com.ggx.core.common.control.GGXSessionDisconnectSupport;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.server.config.GGXCoreServerConfig;

/**
 * ggserver服务器接口
 * 
 * @author zai 2019-12-05 10:41:22
 */
public interface GGXCoreServer extends GGXCoreConfigSupport<GGXCoreServerConfig>, GGXSessionDisconnectSupport{

	/**
	 * 启动服务器
	 * 
	 * @author zai 2019-12-05 10:41:09
	 */
	GGXFuture start();

	/**
	 * 关闭服务器
	 * 
	 * @author zai 2019-12-05 10:41:16
	 */
	GGXFuture shutdown();

}