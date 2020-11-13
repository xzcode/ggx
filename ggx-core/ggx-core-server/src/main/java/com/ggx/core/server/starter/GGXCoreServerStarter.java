package com.ggx.core.server.starter;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.server.config.GGXCoreServerConfig;

/**
 * 统一服务器启动接口
 * 
 * @author zai
 * 2019-12-18 17:27:37
 */
public interface GGXCoreServerStarter {
	
	/**
	 * 启动
	 * 
	 * @return
	 * @author zai
	 * 2019-12-18 17:27:57
	 */
	GGXFuture<?> start();
	
	/**
	 * 关闭
	 * 
	 * @author zai
	 * 2019-12-18 17:28:02
	 */
	GGXFuture<?> shutdown();
	
	/**
	 * 设置配置
	 * 
	 * @param config
	 * @author zai
	 * 2019-12-18 17:28:13
	 */
	void setConfig(GGXCoreServerConfig config);
}
