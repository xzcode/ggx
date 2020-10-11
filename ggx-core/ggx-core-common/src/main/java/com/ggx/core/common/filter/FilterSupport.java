package com.ggx.core.common.filter;

import com.ggx.core.common.filter.model.FilterInfo;
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
	default boolean doReceivePackFilters(Pack pack) {
		return getFilterManager().doReceivePackFilters(pack);
	}

	@Override
	default boolean doReceiveFilters(MessageData request) {
		return getFilterManager().doReceiveFilters(request);
	}

	@Override
	default boolean doSendFilters(MessageData messageData) {
		return getFilterManager().doSendFilters(messageData);
	}

	@Override
	default boolean doSendPackFilters(Pack pack) {
		return getFilterManager().doSendPackFilters(pack);
	}
	
	default void addFilter(Filter<?> filter) {
		getFilterManager().addFilter(new FilterInfo<>(filter));
	}
	
	default void addFilter(Filter<?> filter, int order) {
		getFilterManager().addFilter(new FilterInfo<>(filter, order));
	}
	

	@Override
	default void addFilter(FilterInfo<?> filterInfo) {
		getFilterManager().addFilter(filterInfo);
	}

	@Override
	default void removeFilter(FilterInfo<?> filterInfo) {
		getFilterManager().removeFilter(filterInfo);
	}
	
	

	
}
