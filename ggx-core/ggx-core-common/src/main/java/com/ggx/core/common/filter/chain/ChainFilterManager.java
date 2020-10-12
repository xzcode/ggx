package com.ggx.core.common.filter.chain;

import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

public interface ChainFilterManager {

	

	void doReceiveMessageFilters(MessageData data);

	void doSendMessageFilters(MessageData data);
	
	void doReceivePackFilters(Pack pack);

	void doSendPackFilters(Pack pack);

	void addFilter(ChainFilter<?> filter);
	void removeFilter(ChainFilter<?> filter);

}
