package com.ggx.server.starter;

import com.ggx.core.common.config.GGXCoreSupport;

public interface GGXServerStarter extends GGXCoreSupport{
	
	
	/**
	 * 启动
	 *
	 * @author zzz
	 * 2020-08-21 18:24:48
	 */
	public void start();
	
	
	/**
	 * 关闭
	 *
	 * @author zzz
	 * 2020-08-24 14:29:21
	 */
	public void shutdown();
	
	

}
