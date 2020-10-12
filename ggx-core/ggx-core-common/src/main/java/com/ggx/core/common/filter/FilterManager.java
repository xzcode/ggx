package com.ggx.core.common.filter;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

public interface FilterManager {

	

	GGXFuture doReceiveMessageFilters(MessageData data);

	GGXFuture doSendMessageFilters(MessageData data);
	
	GGXFuture doReceivePackFilters(Pack pack);

	GGXFuture doSendPackFilters(Pack pack);

	void addFilter(Filter<?> filter);
	void removeFilter(Filter<?> filter);

}
