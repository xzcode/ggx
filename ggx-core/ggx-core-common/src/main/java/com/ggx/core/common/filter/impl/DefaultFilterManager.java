package com.ggx.core.common.filter.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.MessageFilter;
import com.ggx.core.common.filter.PackFilter;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;

/**
 * 默认过滤器管理器
 * 
 * @author zai
 * 2019-12-25 15:08:41
 */
public class DefaultFilterManager implements FilterManager {

	private List<FilterInfo<?>> messageFilters = new CopyOnWriteArrayList<>();
	private List<FilterInfo<?>> packFilters = new CopyOnWriteArrayList<>();

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
		if (filter instanceof MessageFilter) {
			return this.messageFilters;
		}
		if (filter instanceof PackFilter) {
			return this.packFilters;
		}
		return null;
		
	}
	
	

	@Override
	public boolean doReceivePackFilters(Pack pack) {
		for (FilterInfo<?> filter : packFilters) {
			if (filter == null || !((PackFilter)filter.getFilter()).doReceiveFilter(pack)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doSendPackFilters(Pack pack) {
		for (FilterInfo<?> filter : packFilters) {
			if (filter == null || !((PackFilter)filter.getFilter()).doSendFilter(pack)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doReceiveFilters(MessageData messageData) {
		for (FilterInfo<?> filter : messageFilters) {
			if (filter == null || !((MessageFilter)filter.getFilter()).doReceiveFilter(messageData)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doSendFilters(MessageData messageData) {
		for (FilterInfo<?> filter : messageFilters) {
			if (filter == null || !((MessageFilter)filter.getFilter()).doSendFilter(messageData)) {
				return false;
			}
		}
		return true;
	}

	

	

}
