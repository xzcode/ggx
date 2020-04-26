package com.xzcode.ggserver.core.server;

import com.ggx.core.common.config.GGConfigSupport;
import com.ggx.core.common.control.IGGContolSupport;
import com.ggx.core.common.future.GGFuture;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * ggserver服务器接口
 * 
 * @author zai 2019-12-05 10:41:22
 */
public interface GGServer extends GGConfigSupport<GGServerConfig> , IGGContolSupport{

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