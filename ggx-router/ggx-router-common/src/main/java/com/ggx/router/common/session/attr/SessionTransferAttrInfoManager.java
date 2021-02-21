package com.ggx.router.common.session.attr;

import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * 会话属性信息管理器
 *
 * 2021-02-21 20:15:18
 */
public class SessionTransferAttrInfoManager extends ListenableMapDataManager<String, SessionAttrInfo>{

	
	/**
	 * 注册
	 *
	 * @param attrInfo
	 * 2021-02-21 20:17:09
	 */
	public void register(SessionAttrInfo attrInfo) {
		this.put(attrInfo.getKey(), attrInfo);
	}
	
	/**
	 * 根据key与Class注册
	 *
	 * @param key
	 * @param fullClassName
	 * 2021-02-21 20:20:45
	 */
	public void register(String key, Class<?> clazz) {
		SessionAttrInfo attrInfo = new SessionAttrInfo(key, clazz);
		this.put(key, attrInfo);
	}
	
	/**
	 * 根据key与完整类名注册
	 *
	 * @param key
	 * @param fullClassName
	 * 2021-02-21 20:20:45
	 */
	public void register(String key, String fullClassName) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullClassName);
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Register session attribute info error!", e);
		}
		SessionAttrInfo attrInfo = new SessionAttrInfo(key, clazz);
		this.put(key, attrInfo);
	}
}
