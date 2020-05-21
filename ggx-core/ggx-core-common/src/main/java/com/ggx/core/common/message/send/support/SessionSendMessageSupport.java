package com.ggx.core.common.message.send.support;

import java.util.concurrent.Future;

import org.slf4j.Logger;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGFailedFuture;
import com.ggx.core.common.future.GGNettyFuture;
import com.ggx.core.common.future.GGFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.json.GGServerJsonUtil;
import com.ggx.core.common.utils.logger.GGLoggerUtil;

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
	FilterManager getFilterManager();

	/**
	 * 获取计划任务执行器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:35:12
	 */
	TaskExecutor getTaskExecutor();

	/**
	 * 获取会话
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:35:24
	 */
	GGSession getSession();
	
	
	/**
	 * 发送消息
	 * 
	 * @param action
	 * @return
	 * @author zai
	 * 2019-12-17 18:44:14
	 */
	default GGFuture send(String action) {
		return send(new MessageData<>((GGSession) this, action, null));
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
	default GGFuture send(String action, Object message) {
		return send(new MessageData<>((GGSession) this, action, message));
	}
	
	/**
	 * 发送消息
	 * 
	 * @param message
	 * @return
	 * @author zai
	 * 2019-12-25 11:57:05
	 */
	default GGFuture send(Message message) {
		return send(new MessageData<>((GGSession) this, message.getActionId(), message));
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
	default GGFuture send(GGSession session, String action, Object message) {
		return send(new MessageData<>(session,  action, message));
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
	default GGFuture send(GGSession session, Message message) {
		return send(new MessageData<>(session, message.getActionId(), message));
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
	default GGFuture send(MessageData<?> messageData) {
		// 发送过滤器
		if (!getFilterManager().doSendFilters(messageData)) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		return send(makePack(messageData));
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
	default GGFuture send(Pack pack) {
		
		GGSession session = pack.getSession();
		
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
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		// 序列化后发送过滤器
		if (!getFilterManager().doAfterSerializeFilters(pack)) {
			return GGFailedFuture.DEFAULT_FAILED_FUTURE;
		}
		
		if (channel.isActive()) {
			GGNettyFuture future = new GGNettyFuture();
			ChannelFuture channelFuture = channel.writeAndFlush(pack);
			future.setFuture((Future<?>) channelFuture);

			return future;
		}
		Logger logger = GGLoggerUtil.getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("Channel is inactived! Message will not be send, Pack:{}", GGServerJsonUtil.toJson(pack));
		}
		return GGFailedFuture.DEFAULT_FAILED_FUTURE;
	}


}
