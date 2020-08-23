package com.xzcode.ggserver.core.server;

import com.ggx.core.common.config.GGXCoreConfigSupport;
import com.ggx.core.common.control.IGGContolSupport;
import com.ggx.core.common.future.GGFuture;
import com.xzcode.ggserver.core.server.config.GGXCoreServerConfig;

/**
 * ggserver服务器接口
 * 
 * @author zai 2019-12-05 10:41:22
 */
public interface GGXCoreServer extends GGXCoreConfigSupport<GGXCoreServerConfig> , IGGContolSupport{

	/**
	 * 启动服务器
	 * 
	 * @author zai 2019-12-05 10:41:09
	 */
	GGFuture start();

	/**
	 * 关闭服务器
	 * 
	 * @author zai 2019-12-05 10:41:16
	 */
	void shutdown();

}