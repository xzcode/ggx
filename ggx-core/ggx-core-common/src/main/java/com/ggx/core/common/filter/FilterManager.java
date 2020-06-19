package com.ggx.core.common.filter;

import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 过滤器管理器统一接口
 * 
 * @author zai
 * 2019-12-25 15:05:30
 */
public interface FilterManager {
	

	boolean doBeforeDeserializeFilters(Pack pack);

	boolean doReceiveFilters(MessageData<?> request);

	boolean doSendFilters(MessageData<?> response);

	boolean doAfterSerializeFilters(Pack pack);
	
	

	void addFilter(FilterInfo<?> filterInfo);

	void removeFilter(FilterInfo<?> filterInfo);


}