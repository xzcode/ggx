package com.ggx.router.client.service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.Pack;
import com.ggx.router.client.service.listener.RouterServiceShutdownListener;

/**
 * 路由服务统一接口
 * 
 * @author zai
 * 2019-11-07 16:51:06
 */
public interface RouterService {
	
	/**
	 * 获取服务id
	 * 
	 * @return
	 * @author zai
	 * 2019-11-07 16:50:36
	 */
	String getServiceId();
	
	
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
	GGFuture dispatch(Pack pack);
	
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


	/**
	 * 添加关闭监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-05-06 15:30:05
	 */
	void addShutdownListener(RouterServiceShutdownListener listener);
	
	/**
	 * 获取当前负载量
	 *
	 * @return
	 * @author zai
	 * 2020-05-06 17:15:31
	 */
	AtomicInteger getLoad();
}
