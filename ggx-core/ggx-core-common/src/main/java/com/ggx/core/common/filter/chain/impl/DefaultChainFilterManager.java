package com.ggx.core.common.filter.chain.impl;

import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.filter.Filter;
import com.ggx.core.common.filter.chain.ReceiveMessageChainFilter;
import com.ggx.core.common.filter.chain.ReceivePackChainFilter;
import com.ggx.core.common.filter.chain.SendMessageChainFilter;
import com.ggx.core.common.filter.chain.SendPackChainFilter;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXSuccessFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.util.logger.GGXLogUtil;

public class DefaultChainFilterManager implements FilterManager{
	
	private MessageFilterChain receiveMessageFilterChain = new MessageFilterChain();
	private MessageFilterChain sendMessageFilterChain = new MessageFilterChain();
	
	private PackFilterChain receivePackFilterChain = new PackFilterChain();
	private PackFilterChain sendPackFilterChain = new PackFilterChain();

	
	@Override
	public GGXFuture doReceiveMessageFilters(MessageData data) {
		try {
			this.receiveMessageFilterChain.doFilter(data);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Receive Message Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}
	
	@Override
	public GGXFuture doSendMessageFilters(MessageData data) {
		try {
			this.sendMessageFilterChain.doFilter(data);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Exception e) {
		GGXLogUtil.getLogger(this).error("Do Send Message Filters Error!", e);
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}
		
	}
	@Override
	public GGXFuture doReceivePackFilters(Pack pack) {
		try {
			this.receivePackFilterChain.doFilter(pack);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Receive Pack Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}

	@Override
	public GGXFuture doSendPackFilters(Pack pack) {
		try {
			this.sendPackFilterChain.doFilter(pack);
			return GGXSuccessFuture.DEFAULT_SUCCESS_FUTURE;
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Send Pack Filters Error!", e);
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addFilter(Filter<?> filter) {
		if (filter instanceof ReceiveMessageChainFilter) {
			this.receiveMessageFilterChain.addFilter((Filter<MessageData>) filter);
			return;
		}
		if (filter instanceof SendMessageChainFilter) {
			this.sendMessageFilterChain.addFilter((Filter<MessageData>) filter);
			return;
		}
		if (filter instanceof ReceivePackChainFilter) {
			this.receivePackFilterChain.addFilter((Filter<Pack>) filter);
			return;
		}
		if (filter instanceof SendPackChainFilter) {
			this.sendPackFilterChain.addFilter((Filter<Pack>) filter);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeFilter(Filter<?> filter) {
		if (filter instanceof ReceiveMessageChainFilter) {
			this.receiveMessageFilterChain.removeFilter((Filter<MessageData>) filter);
			return;
		}
		if (filter instanceof SendMessageChainFilter) {
			this.sendMessageFilterChain.removeFilter((Filter<MessageData>) filter);
			return;
		}
		if (filter instanceof ReceivePackChainFilter) {
			this.receivePackFilterChain.removeFilter((Filter<Pack>) filter);
			return;
		}
		if (filter instanceof SendPackChainFilter) {
			this.sendPackFilterChain.removeFilter((Filter<Pack>) filter);
			return;
		}
	}

	
	
}
