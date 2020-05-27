package com.ggx.game.card.game.support.room.filter;

import com.ggx.core.common.filter.ReceiveMessageFilter;
import com.ggx.core.common.message.MessageData;
import com.ggx.core.common.message.receive.manager.ReceiveMessageManager;
import com.ggx.core.common.session.GGSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.game.card.game.support.house.House;
import com.ggx.game.card.game.support.player.RoomPlayer;
import com.ggx.game.card.game.support.room.SingleThreadExecutorRoom;

/**
 * 单线程执行器房间操作过滤器
 * <br>
 * 用于绑定用户操作到房间内的单线程执行器
 * 
 * @param <P>
 * @param <R>
 * @param <H>
 * @author zai
 * 2019-12-22 21:46:57
 */
public class SingleThreadExecutorRoomOperactionFilter
<
P extends RoomPlayer<P, R, H>,
R extends SingleThreadExecutorRoom<P, R, H>,
H extends House<P, R, H>
> implements ReceiveMessageFilter {
	
	protected ReceiveMessageManager requestMessageManager;
	
	protected String playerSessionKey;

	public SingleThreadExecutorRoomOperactionFilter(String playerSessionKey, ReceiveMessageManager requestMessageManager) {
		this.playerSessionKey = playerSessionKey;
		this.requestMessageManager = requestMessageManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean doFilter(MessageData<?> messageData) {
		GGSession session = messageData.getSession();
		if (session == null) {
			return true;
		}
		P player = (P) session.getAttribute(playerSessionKey);
		if (player == null) {
			return true;
		}
		R room = player.getRoom();
		if (room != null) {
			room.addOperaction((r) -> {
				try {
					requestMessageManager.handle(messageData);
				} catch (Exception e) {
					GGLoggerUtil.getLogger(this).error("Request Filter Error!", e);
				}
			});
			return false;
		}
		return true;
	}

}
