package com.ggx.core.common.message.send.support;

import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.Pack;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.util.logger.GGXLoggerUtil;

/**
 * 消息发送接口
 * 
 * 
 * @author zai 2019-02-09 14:50:27
 */
public interface SendMessageSupport extends MakePackSupport {
	
	/**
	 * 获取会话管理器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:33:54
	 */
	SessionManager getSessionManager();
	
	/**
	 * 获取过滤器管理器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-11 16:34:01
	 */
	FilterManager getFilterManager();


	/**
	 * 发送给所有会话
	 * @param response
	 * 
	 * @author zai
	 * 2019-11-27 22:09:14
	 */
	default void sendToAll(MessageData<?> response) {
		try {
			// 发送过滤器
			if (!getFilterManager().doSendFilters(response)) {
				return;
			}

			Pack pack = makePack(response);
			SessionManager sessionManager = getSessionManager();
			sessionManager.eachSession(session -> {
				session.send(pack);
				return true;
			});
		} catch (Exception e) {
			GGXLoggerUtil.getLogger().error("GGServer sendToAll ERROR!");
		}

	}
	
	/**
	 * 发送给指定会话
	 * @param session
	 * @param pack
	 * @param delay
	 * @param timeUnit
	 * @return
	 * 
	 * @author zai
	 * 2019-11-27 22:09:31
	 */
	default GGXFuture send(GGXSession session, Pack pack) {
		return session.send(pack);
	}
	
	
	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param action
	 * @param message
	 * @return
	 * @author zai
	 * 2019-11-29 15:24:23
	 */
	default GGXFuture send(GGXSession session, String action, Message message) {
		return send(new MessageData<>(session, action, message));
	}
	
	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param action
	 * @param message
	 * @param delay
	 * @param timeUnit
	 * @return
	 * @author zai
	 * 2019-11-29 15:23:47
	 */
	default GGXFuture send(GGXSession session, Message message) {
		return send(new MessageData<>(session, message.getActionId(), message));
	}

	/**
	 * 发送消息
	 * @param session
	 * @param response
	 * @param delay
	 * @param timeUnit
	 * @return
	 * 
	 * @author zai
	 * 2019-11-27 21:53:08
	 */
	default GGXFuture send(MessageData<?> messageData) {
		GGXSession session = messageData.getSession();
		if (session != null) {
			try {
			// 发送过滤器
			if (!getFilterManager().doSendFilters(messageData)) {
				return null;
			}
				session.send(makePack(messageData));
			} catch (Exception e) {
				GGXLoggerUtil.getLogger().error("Send message Error!", e);
			}
		}
		return null;
	}

	/**
	 * 发送包
	 * 
	 * @param pack
	 * @return
	 * @author zai
	 * 2019-12-16 10:20:47
	 */
	default GGXFuture send(Pack pack) {
		return send(null, pack);
	}
	
	
}
