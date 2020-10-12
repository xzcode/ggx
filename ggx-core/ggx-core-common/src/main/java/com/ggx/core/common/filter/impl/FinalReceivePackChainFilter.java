package com.ggx.core.common.filter.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.ReceivePackFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.receive.controller.MessageControllerManager;
import com.ggx.core.common.message.receive.controller.model.ControllerMethodInfo;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.serializer.factory.SerializerFactory;
import com.ggx.util.logger.GGXLogUtil;

public class FinalReceivePackChainFilter implements ReceivePackFilter{
	
	
	
	
	private GGXCoreConfig config;

	public FinalReceivePackChainFilter(GGXCoreConfig config) {
		this.config = config;
	}




	@Override
	public void doFilter(Pack pack, FilterChain<Pack> filterChain) {
		
		Message message = null;
		String action = null;
		
		try {
			action = new String(pack.getAction(), config.getCharset());
			Serializer serializer = config.getSerializer();
			MessageControllerManager messageControllerManager = config.getMessageControllerManager();
			ControllerMethodInfo methodInfo = messageControllerManager.getMethodInfo(action);
			if (pack.getMessage() != null) {
				if (messageControllerManager.getMethodInfo(action) != null) {
					if (pack.getSerializeType() != null) {
						Serializer getSerializer = SerializerFactory.getSerializer(pack.getSerializeType());
						if (getSerializer != null) {
							message = (Message) getSerializer.deserialize(pack.getMessage(),
									methodInfo.getMessageClass());
						}
					} else {
						message = (Message) serializer.deserialize(pack.getMessage(), methodInfo.getMessageClass());
					}
				}
			}
			MessageData messageData = new MessageData(pack.getSession(), action, message);
			config.getFilterManager().doReceiveMessageFilters(messageData);
		} catch (Exception e) {
			GGXLogUtil.getLogger().error("FinalReceivePackChainFilter ERROR!! -- actionId: {}, error: {}", action, e);
		}
	}

}
