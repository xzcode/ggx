package com.ggx.core.common.filter.impl;

import java.nio.charset.Charset;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.SendMessageFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.send.support.MakePackSupport;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.util.logger.GGXLogUtil;

public class FinalSendMessageChainFilter implements SendMessageFilter, MakePackSupport{

	private GGXCoreConfig config;

	public FinalSendMessageChainFilter(GGXCoreConfig config) {
		this.config = config;
	}

	@Override
	public GGXFuture<?> doFilter(MessageData messageData, FilterChain<MessageData> filterChain) {
		GGXSession session = messageData.getSession();
		if (session != null) {
			try {
				Pack pack = session.makePack(messageData);
				return session.send(pack);
			} catch (Exception e) {
				GGXLogUtil.getLogger().error("FinalSendMessageChainFilter Error!", e);
			}
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}

	@Override
	public Charset getCharset() {
		return config.getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return config.getSerializer();
	}

}
