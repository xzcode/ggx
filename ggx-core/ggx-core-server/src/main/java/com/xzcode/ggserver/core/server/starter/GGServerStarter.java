package com.xzcode.ggserver.core.server.starter;

import com.ggx.core.common.future.GGFuture;
import com.xzcode.ggserver.core.server.config.GGServerConfig;

/**
 * 统一服务器启动接口
 * 
 * @author zai
 * 2019-12-18 17:27:37
 */
public interface GGServerStarter {
	
	/**
	 * 启动
	 * 
	 * @return
	 * @author zai
	 * 2019-12-18 17:27:57
	 */
	GGFuture start();
	
	/**
	 * 关闭
	 * 
	 * @author zai
	 * 2019-12-18 17:28:02
	 */
	void shutdown();
	
	/**
	 * 设置配置
	 * 
	 * @param config
	 * @author zai
	 * 2019-12-18 17:28:13
	 */
	void setConfig(GGServerConfig config);
}
