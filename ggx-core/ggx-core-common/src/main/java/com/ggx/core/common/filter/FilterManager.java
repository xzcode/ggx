package com.ggx.core.common.filter;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

public interface FilterManager {

	GGXFuture<?> doReceiveMessageFilters(MessageData data);

	GGXFuture<?> doSendMessageFilters(MessageData data);

	GGXFuture<?> doReceivePackFilters(Pack pack);

	GGXFuture<?> doSendPackFilters(Pack pack);

	FilterInfo<?> addFilter(FilterInfo<?> filter);

	FilterInfo<?> addFilter(Filter<?> filter, int order);

	FilterInfo<?> addFilter(Filter<?> filter);

	void removeFilter(FilterInfo<?> filter);
}
