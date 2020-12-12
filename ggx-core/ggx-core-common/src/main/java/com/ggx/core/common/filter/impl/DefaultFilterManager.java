package com.ggx.core.common.filter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.ReceivePackFilter;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.filter.SendPackFilter;
import com.ggx.core.common.filter.chain.impl.MessageFilterChain;
import com.ggx.core.common.filter.chain.impl.PackFilterChain;
import com.ggx.core.common.filter.model.FilterInfo;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXSuccessFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.util.logger.GGXLogUtil;

public class DefaultFilterManager implements FilterManager {

	private PackFilterChain receivePackFilterChain = new PackFilterChain();
	private MessageFilterChain receiveMessageFilterChain = new MessageFilterChain();
	private PackFilterChain sendPackFilterChain = new PackFilterChain();
	private MessageFilterChain sendMessageFilterChain = new MessageFilterChain();


	private ReceivePackFilter finalReceivePackFilter;

	private ReceiveMessageFilter finalReceiveMessageFilter;

	private SendMessageFilter finalSendMessageChainFilter;

	private SendPackFilter finalSendPackChainFilter;

	private List<FilterInfo<?>> receivePackFilterInfos = new ArrayList<>();
	private List<FilterInfo<?>> receiveMessageFilterInfos = new ArrayList<>();
	private List<FilterInfo<?>> sendMessageFilterInfos = new ArrayList<>();
	private List<FilterInfo<?>> sendPackFilterInfos = new ArrayList<>();

	public DefaultFilterManager(ReceivePackFilter finalReceivePackFilter,
			ReceiveMessageFilter finalReceiveMessageFilter, SendMessageFilter finalSendMessageChainFilter,
			SendPackFilter finalSendPackChainFilter) {
		
		this.finalReceivePackFilter = finalReceivePackFilter;
		receivePackFilterChain.addFilter(finalReceivePackFilter);
		
		this.finalReceiveMessageFilter = finalReceiveMessageFilter;
		receiveMessageFilterChain.addFilter(finalReceiveMessageFilter);
		
		this.finalSendMessageChainFilter = finalSendMessageChainFilter;
		sendMessageFilterChain.addFilter(finalSendMessageChainFilter);
		
		this.finalSendPackChainFilter = finalSendPackChainFilter;
		sendPackFilterChain.addFilter(finalSendPackChainFilter);
	}

	@Override
	public GGXFuture<?> doReceiveMessageFilters(MessageData data) {
		try {
			this.receiveMessageFilterChain.doFilter(data);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("Do Receive Message Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}

	@Override
	public GGXFuture<?> doSendMessageFilters(MessageData data) {
		try {
			this.sendMessageFilterChain.doFilter(data);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("Do Send Message Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}

	}

	@Override
	public GGXFuture<?> doReceivePackFilters(Pack pack) {
		try {
			this.receivePackFilterChain.doFilter(pack);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("Do Receive Pack Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}

	@Override
	public GGXFuture<?> doSendPackFilters(Pack pack) {
		try {
			this.sendPackFilterChain.doFilter(pack);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Throwable e) {
			GGXLogUtil.getLogger(this).error("Do Send Pack Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public FilterInfo<?> addFilter(FilterInfo<?> filterInfo) {

		Filter<?> filter = filterInfo.getFilter();

		if (filter instanceof ReceivePackFilter) {
			this.receivePackFilterInfos.add((FilterInfo<Pack>) filterInfo);
			List list = sortFiltersAndGetFilterList(this.receivePackFilterInfos);
			list.add(finalReceivePackFilter);
			receivePackFilterChain.setFilters(list);
			
		}else
		if (filter instanceof ReceiveMessageFilter) {
			this.receiveMessageFilterInfos.add(filterInfo);
			List list = sortFiltersAndGetFilterList(this.receiveMessageFilterInfos);
			list.add(finalReceiveMessageFilter);
			receiveMessageFilterChain.setFilters(list);
		}else
		if (filter instanceof SendMessageFilter) {
			this.sendMessageFilterInfos.add((FilterInfo<MessageData>) filterInfo);
			List list = sortFiltersAndGetFilterList(this.sendMessageFilterInfos);
			list.add(finalSendMessageChainFilter);
			sendMessageFilterChain.setFilters(list);
		}else
		if (filter instanceof SendPackFilter) {
			this.sendPackFilterInfos.add(filterInfo);
			List list = sortFiltersAndGetFilterList(this.sendPackFilterInfos);
			list.add(finalSendPackChainFilter);
			sendPackFilterChain.setFilters(list);
		}
		return filterInfo;
	}
	
	

	@Override
	public FilterInfo<?> addFilter(Filter<?> filter, int order) {
		return addFilter(new FilterInfo<>(filter, order));
	}

	@Override
	public FilterInfo<?> addFilter(Filter<?> filter) {
		return addFilter(new FilterInfo<>(filter));
	}
	


	@Override
	public void removeFilter(FilterInfo<?> filterInfo) {
		Filter<?> filter = filterInfo.getFilter();

		if (filter instanceof ReceivePackFilter) {
			this.receivePackFilterInfos.remove(filterInfo);
			return;
		}
		if (filter instanceof ReceiveMessageFilter) {
			this.receiveMessageFilterInfos.remove(filterInfo);
			return;
		}
		if (filter instanceof SendMessageFilter) {
			this.sendMessageFilterInfos.remove(filterInfo);
			return;
		}
		if (filter instanceof SendPackFilter) {
			this.sendPackFilterInfos.remove(filterInfo);
			return;
		}
	}
	
	public void sortFilters(List<FilterInfo<?>> filterInfos) {
		filterInfos.sort((a, b) -> {
			return a.getOrder() - b.getOrder();
		});
	}
	public List<Filter<?>> sortFiltersAndGetFilterList(List<FilterInfo<?>> filterInfos) {
		filterInfos.sort((a, b) -> {
			return a.getOrder() - b.getOrder();
		});
		return filterInfos.stream().map(e -> e.getFilter()).collect(Collectors.toList());
	}

}
