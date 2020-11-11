package com.ggx.rpc.client.service;

import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcServiceClassCache extends ListenableMapDataManager<String, Class<?>> {

	@Override
	public Class<?> get(String key) {
		Class<?> clazz = super.get(key);
		if (clazz == null) {
			try {
				clazz = Class.forName(key);
			} catch (ClassNotFoundException e) {
				GGXLogUtil.getLogger(this).error("Class Not Found!", e);
			}
			if (clazz != null) {
				super.put(key, clazz);
			}
		}
		return clazz;
	}

}
