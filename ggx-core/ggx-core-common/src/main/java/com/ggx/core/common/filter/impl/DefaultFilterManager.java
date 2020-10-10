package com.ggx.core.common.filter.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.filter.AfterSerializeFilter;
import com.ggx.core.common.filter.BeforeDeserializeFilter;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.FilterChain;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;

/**
 * 默认过滤器管理器
 * 
 * @author zai
 * 2019-12-25 15:08:41
 */
public class DefaultFilterManager implements FilterManager, FilterChain {

	private List<FilterInfo<?>> beforeDeserializeFilters = new CopyOnWriteArrayList<>();
	private List<FilterInfo<?>> receiveMessageFilters = new CopyOnWriteArrayList<>();
	private List<FilterInfo<?>> sendMessageFilters = new CopyOnWriteArrayList<>();
	private List<FilterInfo<?>> afterSerializeFilters = new CopyOnWriteArrayList<>();

	public DefaultFilterManager() {
		super();
	}

	@Override
	public void addFilter(FilterInfo<?> filterInfo) {
		this.getFilterList(filterInfo.getFilter()).add(filterInfo);
	}

	@Override
	public void removeFilter(FilterInfo<?> filterInfo) {
		this.getFilterList(filterInfo.getFilter()).remove(filterInfo);
	}
	
	private List<FilterInfo<?>> getFilterList(Filter<?> filter) {
		if (filter instanceof BeforeDeserializeFilter) {
			return this.beforeDeserializeFilters;
		}
		if (filter instanceof ReceiveMessageFilter) {
			return this.receiveMessageFilters;
		}
		if (filter instanceof SendMessageFilter) {
			return this.sendMessageFilters;
		}
		if (filter instanceof AfterSerializeFilter) {
			return this.afterSerializeFilters;
		}
		return null;
		
	}
	
	

	@Override
	public boolean doBeforeDeserializeFilters(Pack pack) {
		for (FilterInfo<?> filter : beforeDeserializeFilters) {
			if (filter == null || !((BeforeDeserializeFilter)filter.getFilter()).doFilter(pack)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doAfterSerializeFilters(Pack pack) {
		for (FilterInfo<?> filter : afterSerializeFilters) {
			if (filter == null || !((AfterSerializeFilter)filter.getFilter()).doFilter(pack)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doReceiveFilters(MessageData<?> messageData) {
		for (FilterInfo<?> filter : receiveMessageFilters) {
			if (filter == null || !((ReceiveMessageFilter)filter.getFilter()).doFilter(messageData)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doSendFilters(MessageData<?> messageData) {
		for (FilterInfo<?> filter : sendMessageFilters) {
			if (filter == null || !((SendMessageFilter)filter.getFilter()).doFilter(messageData)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void doFilter(MessageData<? extends Message> data) {
		
	}

	

}
