package com.ggx.registry.common.service.listener;

import com.ggx.registry.common.service.ServiceInfo;

/**
 * 注册服务监听器接口
 * 
 * @author zai
 * 2020-02-06 15:05:30
 */
public interface RegisterServiceListener {
	
	/**
	 * 注册服务时执行
	 * 
	 * @param service
	 * @author zai
	 * 2020-02-06 15:07:24
	 */
	void onRegister(ServiceInfo service);

}
