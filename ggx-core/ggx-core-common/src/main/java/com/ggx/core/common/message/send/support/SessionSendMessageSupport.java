package com.ggx.core.common.message.send.support;

import java.nio.charset.Charset;
import java.util.concurrent.Future;

import org.slf4j.Logger;

import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXNettyFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.actionid.ActionIdCacheManager;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.serializer.Serializer;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.utils.json.GGXServerJsonUtil;
import com.ggx.util.logger.GGXLogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * 消息发送接口
 * 
 * 
 * @author zai 2019-02-09 14:50:27
 */
public interface SessionSendMessageSupport extends MakePackSupport {
	
	/**
	 * 获取过滤器管理器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:35:06
	 */
	default FilterManager getFilterManager() {
		return getSession().getFilterManager();
	}

	
	
	@Override
	default Charset getCharset() {
		return getSession().getCharset();
	}
	
	@Override
	default Serializer getSerializer() {
		return getSession().getSerializer();
	}
	
	/**
	 * 获取actionId缓存管理器
	 *
	 * @return
	 * @author zai
	 * 2020-10-09 18:08:52
	 */
	default ActionIdCacheManager getActionIdCacheManager() {
		return getSession().getActionIdCacheManager();
	}

	/**
	 * 获取会话
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:35:24
	 */
	GGXSession getSession();
	
	
	default GGXFuture send(Class<? extends Message> actionIdOnlyMessage) {
		return send(new MessageData(getSession(), getActionIdCacheManager().get(actionIdOnlyMessage), null));
	}
	
	
	/**
	 * 发送消息
	 * 
	 * @param action
	 * @return
	 * @author zai
	 * 2019-12-17 18:44:14
	 */
	default GGXFuture send(String action) {
		return send(new MessageData(getSession(), action, null));
	}
	
	/**
	 * 发送消息
	 * 
	 * @param action
	 * @param message
	 * @return
	 * @author zai
	 * 2019-12-17 18:44:20
	 */
	default GGXFuture send(String action, Message message) {
		return send(new MessageData(getSession(), action, message));
	}
	
	/**
	 * 发送消息
	 * 
	 * @param message
	 * @return
	 * @author zai
	 * 2019-12-25 11:57:05
	 */
	default GGXFuture send(Message message) {
		return send(new MessageData(getSession(), getActionIdCacheManager().get(message.getClass()), message));
	}
	
	
	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param action
	 * @param message
	 * @return
	 * @author zai
	 * 2019-11-29 15:26:11
	 */
	default GGXFuture send(GGXSession session, String action, Message message) {
		return send(new MessageData(session,  action, message));
	}
	
	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param message
	 * @return
	 * @author zai
	 * 2019-12-25 11:57:44
	 */
	default GGXFuture send(GGXSession session, Message message) {
		return send(new MessageData(session, getActionIdCacheManager().get(message.getClass()), message));
	}
	
	
	/**
	 * 发送消息
	 * 
	 * @param response 响应消息
	 * @param delay    延迟时间
	 * @param timeUnit 时间单位
	 * @return
	 * 
	 * @author zai 2019-11-24 17:29:24
	 */
	default GGXFuture send(MessageData messageData) {
		if (messageData.getSession() == null) {
			messageData.setSession(getSession());			
		}
		// 发送过滤器
		return getFilterManager().doSendMessageFilters(messageData);
	}
	
	/**
	 * 发送消息
	 * 
	 * @param pack
	 * @param delay
	 * @param timeUnit
	 * @return
	 * 
	 * @author zai 2019-11-24 23:08:36
	 */
	default GGXFuture send(Pack pack) {
		
		GGXSession session = pack.getSession();
		
		if (session == null) {
			session = getSession();
		}
		
		Channel channel = null;
		if (session != null) {
			channel = session.getChannel();
		}
		if (channel == null) {
			channel = pack.getChannel();
		}
		
		if (channel == null || !channel.isActive()) {
			return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		// 序列化后发送过滤器
		getFilterManager().doSendPackFilters(pack);
		
		if (channel.isActive()) {
			GGXNettyFuture future = new GGXNettyFuture();
			ChannelFuture channelFuture = channel.writeAndFlush(pack);
			future.setFuture((Future<?>) channelFuture);

			return future;
		}
		Logger logger = GGXLogUtil.getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("Channel is inactived! Message will not be send, Pack:{}", GGXServerJsonUtil.toJson(pack));
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}


}
