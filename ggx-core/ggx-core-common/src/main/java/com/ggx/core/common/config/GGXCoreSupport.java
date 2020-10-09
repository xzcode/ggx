package com.ggx.core.common.config;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.manager.SessionManager;


public interface GGXCoreSupport extends 
GGXCore

{
	
	GGXCore getGGXCore();
	

	@Override
	default TaskExecutor getTaskExecutor() {
		return getGGXCore().getTaskExecutor();
	}
	
	@Override
	default ActionIdCacheManager getActionIdCacheManager() {
		return getGGXCore().getActionIdCacheManager();
	}
	
	@Override
	default MessageControllerManager getMessageControllerManager() {
		return getGGXCore().getMessageControllerManager();
	}	


	@Override
	default SessionManager getSessionManager() {
		return getGGXCore().getSessionManager();
	}

	@Override
	default FilterManager getFilterManager() {
		return getGGXCore().getFilterManager();
	}

	@Override
	default EventManager getEventManager() {
		return getGGXCore().getEventManager();
	}

	@Override
	default Charset getCharset() {
		return getGGXCore().getCharset();
	}

	@Override
	default Serializer getSerializer() {
		return getGGXCore().getSerializer();
	}

	

}
