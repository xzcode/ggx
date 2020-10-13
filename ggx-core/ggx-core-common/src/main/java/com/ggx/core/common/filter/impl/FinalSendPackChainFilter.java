package com.ggx.core.common.filter.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.SendPackFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.utils.json.GGXServerJsonUtil;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.Channel;

public class FinalSendPackChainFilter implements SendPackFilter{
	
	
	protected GGXCoreConfig config;

	public FinalSendPackChainFilter(GGXCoreConfig config) {
		this.config = config;
	}
	
	@Override
	public void doFilter(Pack pack, FilterChain<Pack> filterChain) {
		GGXSession session = pack.getSession();
		
		Channel channel = null;
		if (session != null) {
			channel = session.getChannel();
		}
		if (channel == null) {
			channel = pack.getChannel();
		}
		
		if (channel == null || !channel.isActive()) {
			if (GGXLogUtil.isDebugEnabled()) {
				GGXLogUtil.getLogger(this).debug("Channel is inactived! Message will not be send, Pack:{}", GGXServerJsonUtil.toJson(pack));
			}
			return;
		}
		
		if (channel.isActive()) {
			channel.writeAndFlush(pack)
			.addListener(f -> {
				if (f.isDone() && !f.isSuccess() && f.cause() != null) {
					GGXLogUtil.getLogger(this).warn("Send Message Failed!!", f.cause());
				}
			});

		}
	}

}
