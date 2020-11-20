package com.ggx.game.support.core.player.support;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.message.send.support.SessionSendMessageSupport;
import com.ggx.core.common.session.listener.SessionDisconnectListener;

/**
 * ggx游戏玩家支持接口
 *
 * @author zai
 * 2020-09-10 16:50:48
 */
public interface GGXPlayerSupport extends SessionSendMessageSupport, ExecutorSupport{
	
	
	
	
	@Override
	default TaskExecutor getTaskExecutor() {
		return getSession().getTaskExecutor();
	}

	/**
	 * 断开连接
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 16:49:11
	 */
	default GGXFuture<?> disconnect() {
		return getSession().disconnect();
	}
	
	/**
	 * 添加断开连接监听器
	 *
	 * @param listener
	 * @return
	 * @author zai
	 * 2020-09-10 16:50:24
	 */
	default void addDisconnectListener(SessionDisconnectListener listener) {
		getSession().addDisconnectListener(listener);
	}
	
}
