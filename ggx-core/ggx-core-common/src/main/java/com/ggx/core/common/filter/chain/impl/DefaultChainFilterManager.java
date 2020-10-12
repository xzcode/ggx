package com.ggx.core.common.filter.chain.impl;

import com.ggx.core.common.filter.chain.ChainFilter;
import com.ggx.core.common.filter.chain.ChainFilterManager;
import com.ggx.core.common.filter.chain.ReceiveMessageChainFilter;
import com.ggx.core.common.filter.chain.ReceivePackChainFilter;
import com.ggx.core.common.filter.chain.SendMessageChainFilter;
import com.ggx.core.common.filter.chain.SendPackChainFilter;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.util.logger.GGXLogUtil;

public class DefaultChainFilterManager implements ChainFilterManager{
	
	private MessageFilterChain receiveMessageFilterChain = new MessageFilterChain();
	private MessageFilterChain sendMessageFilterChain = new MessageFilterChain();
	
	private PackFilterChain receivePackFilterChain = new PackFilterChain();
	private PackFilterChain sendPackFilterChain = new PackFilterChain();

	
	@Override
	public void doReceiveMessageFilters(MessageData data) {
		try {
			this.receiveMessageFilterChain.doFilter(data);			
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Receive Message Filters Error!", e);
		}
	}
	
	@Override
	public void doSendMessageFilters(MessageData data) {
		try {
			this.sendMessageFilterChain.doFilter(data);
		} catch (Exception e) {
		GGXLogUtil.getLogger(this).error("Do Send Message Filters Error!", e);
	}
		
	}
	@Override
	public void doReceivePackFilters(Pack pack) {
		try {
			this.receivePackFilterChain.doFilter(pack);
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Receive Pack Filters Error!", e);
		}
	}

	@Override
	public void doSendPackFilters(Pack pack) {
		try {
			this.sendPackFilterChain.doFilter(pack);
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Do Send Pack Filters Error!", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addFilter(ChainFilter<?> filter) {
		if (filter instanceof ReceiveMessageChainFilter) {
			this.receiveMessageFilterChain.addFilter((ChainFilter<MessageData>) filter);
			return;
		}
		if (filter instanceof SendMessageChainFilter) {
			this.sendMessageFilterChain.addFilter((ChainFilter<MessageData>) filter);
			return;
		}
		if (filter instanceof ReceivePackChainFilter) {
			this.receivePackFilterChain.addFilter((ChainFilter<Pack>) filter);
			return;
		}
		if (filter instanceof SendPackChainFilter) {
			this.sendPackFilterChain.addFilter((ChainFilter<Pack>) filter);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeFilter(ChainFilter<?> filter) {
		if (filter instanceof ReceiveMessageChainFilter) {
			this.receiveMessageFilterChain.removeFilter((ChainFilter<MessageData>) filter);
			return;
		}
		if (filter instanceof SendMessageChainFilter) {
			this.sendMessageFilterChain.removeFilter((ChainFilter<MessageData>) filter);
			return;
		}
		if (filter instanceof ReceivePackChainFilter) {
			this.receivePackFilterChain.removeFilter((ChainFilter<Pack>) filter);
			return;
		}
		if (filter instanceof SendPackChainFilter) {
			this.sendPackFilterChain.removeFilter((ChainFilter<Pack>) filter);
			return;
		}
	}

	
	
}
