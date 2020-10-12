package com.ggx.core.common.filter;

import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 过滤器支持接口
 * 
 * 
 * @author zai
 * 2019-12-01 16:56:07
 */
public interface FilterSupport extends FilterManager{
	
	/**
	 * 获取过滤器管理器
	 * @return
	 * 
	 * @author zai
	 * 2019-12-01 16:56:15
	 */
	FilterManager getFilterManager();

	@Override
	default GGXFuture doReceiveMessageFilters(MessageData data) {
		return getFilterManager().doReceiveMessageFilters(data);
	}

	@Override
	default GGXFuture doSendMessageFilters(MessageData data) {
		return getFilterManager().doSendMessageFilters(data);
	}

	@Override
	default GGXFuture doReceivePackFilters(Pack pack) {
		return getFilterManager().doReceivePackFilters(pack);
	}

	@Override
	default GGXFuture doSendPackFilters(Pack pack) {
		return getFilterManager().doSendPackFilters(pack);
	}

	@Override
	default FilterInfo<?> addFilter(FilterInfo<?> filter) {
		return getFilterManager().addFilter(filter);
	}
	@Override
	default FilterInfo<?> addFilter(Filter<?> filter, int order) {
		return getFilterManager().addFilter(new FilterInfo<>(filter, order));
	}
	@Override
	default FilterInfo<?> addFilter(Filter<?> filter) {
		return getFilterManager().addFilter(new FilterInfo<>(filter, 1));
	}

	@Override
	default void removeFilter(FilterInfo<?> filter) {
		getFilterManager().removeFilter(filter);
	}

		
}
