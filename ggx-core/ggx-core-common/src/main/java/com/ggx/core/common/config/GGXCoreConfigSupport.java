package com.ggx.core.common.config;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.manager.SessionManager;

/**
 * 配置获取支持
 * 
 * @param <C>
 * @author zai 2019-12-11 10:11:31
 */
public interface GGXCoreConfigSupport<C extends GGXCoreConfig> 
extends
GGXCore

{

	/**
	 * 获取配置对象
	 * 
	 * @return
	 * @author zai 2019-12-11 10:12:59
	 */
	C getConfig();

	@Override
	default Serializer getSerializer() {
		return getConfig().getSerializer();
	}

	@Override
	default EventManager getEventManager() {
		return getConfig().getEventManager();
	}

	@Override
	default TaskExecutor getTaskExecutor() {
		return getConfig().getTaskExecutor();
	}

	@Override
	default ReceiveMessageManager getReceiveMessageManager() {
		return getConfig().getReceiveMessageManager();
	}

	default SessionManager getSessionManager() {
		return getConfig().getSessionManager();
	}

	@Override
	default FilterManager getFilterManager() {
		return getConfig().getFilterManager();
	}

	@Override
	default Charset getCharset() {
		return getConfig().getCharset();
	}

}
