package com.ggx.core.common.config;

import java.nio.charset.Charset;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
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
	default ReceiveMessageManager getReceiveMessageManager() {
		return getGGXCore().getReceiveMessageManager();
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
