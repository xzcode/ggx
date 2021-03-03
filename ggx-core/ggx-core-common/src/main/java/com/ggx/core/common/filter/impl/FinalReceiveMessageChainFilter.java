package com.ggx.core.common.filter.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.filter.chain.FilterChain;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;

public class FinalReceiveMessageChainFilter implements ReceiveMessageFilter{

	
	private GGXCoreConfig config;

	public FinalReceiveMessageChainFilter(GGXCoreConfig config) {
		this.config = config;
	}
	
	@Override
	public GGXFuture<?> doFilter(MessageData data, FilterChain<MessageData> filterChain) throws Throwable {
		
		Object returnObject = config.getMessageControllerManager().invoke(data);
		
		GGXSession session = data.getSession();
		int requestSeq = data.getRequestSeq();
		
		GGXCoreFuture<?> returnFuture = new GGXCoreFuture<>();
		if (returnObject != null) {
			if (returnObject instanceof Message) {
				MessageData messageData = new MessageData(session, (Message)returnObject, requestSeq);
				session.send(messageData);
				returnFuture.setDone(true, null, null, true);
			}else if (returnObject instanceof GGXFuture) {
				GGXFuture<?> fu = (GGXFuture<?>) returnObject;
				fu.addListener(f -> {
					Object returnData = fu.get();
					if (returnData != null && returnData instanceof Message) {
						MessageData messageData = new MessageData(session, (Message)returnData, requestSeq);
						session.send(messageData);
					}
					returnFuture.follow(fu);
				});
			}
		}
		
		return returnFuture;
	}

}
