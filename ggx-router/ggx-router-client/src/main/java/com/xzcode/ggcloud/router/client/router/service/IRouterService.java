package com.xzcode.ggcloud.router.client.router.service;

import java.util.Map;

import com.xzcode.ggserver.core.common.future.IGGFuture;
import com.xzcode.ggserver.core.common.message.Pack;

/**
 * 路由服务统一接口
 * 
 * @author zai
 * 2019-11-07 16:51:06
 */
public interface IRouterService {
	
	/**
	 * 获取服务id
	 * 
	 * @return
	 * @author zai
	 * 2019-11-07 16:50:36
	 */
	String getServiceId();
	
	/**
	 * 获取服务匹配器
	 * 
	 * @return
	 * @author zai
	 * 2019-11-07 16:50:29
	 */
	IRouterServiceMatcher getServiceMatcher();
	
	/**
	 * 获取服务地址
	 * 
	 * @return
	 * @author zai
	 * 2019-11-07 16:50:42
	 */
	String getHost();
	
	/**
	 * 获取服务端口
	 * 
	 * @return
	 * @author zai
	 * 2019-11-07 16:50:57
	 */
	int getPort();
	
	
	/**
	 * 进行消息转发
	 * @param pack
	 * 
	 * @author zai
	 * 2019-11-11 21:40:46
	 */
	IGGFuture dispatch(Pack pack);
	
	/**
	 * 移除额外数据
	 * 
	 * @param key
	 * @author zai
	 * 2020-01-13 17:37:14
	 */
	public void removeExtraData(String key);
	
	/**
	 * 获取额外数据
	 * 
	 * @param key
	 * @author zai
	 * 2020-02-07 11:01:55
	 */
	public String getExtraData(String key);
	
	/**
	 * 添加额外数据
	 * 
	 * @param key
	 * @param data
	 * @author zai
	 * 2020-01-13 17:37:08
	 */
	public void addExtraData(String key, String data);
	
	/**
	 * 添加额外数据集合
	 * 
	 * @param extraData
	 * @author zai
	 * 2020-02-06 16:42:05
	 */
	public void addAllExtraData(Map<String, String> extraData);
	
	/**
	 * 获取额外数据集合
	 * 
	 * @return
	 * @author zai
	 * 2020-01-13 17:36:46
	 */
	public Map<String, String> getExtraDatas();
	
	/**
	 * 关闭服务
	 * 
	 * @author zai
	 * 2020-02-06 16:47:04
	 */
	public void shutdown();

	/**
	 * 是否可用
	 * 
	 * @return
	 * @author zai
	 * 2020-02-14 12:41:17
	 */
	boolean isAvailable();
}
