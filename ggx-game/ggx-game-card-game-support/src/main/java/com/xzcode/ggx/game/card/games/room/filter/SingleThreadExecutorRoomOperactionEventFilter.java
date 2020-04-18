package com.xzcode.ggx.game.card.games.room.filter;

import com.xzcode.ggserver.core.common.event.EventManager;
import com.xzcode.ggserver.core.common.event.model.EventData;
import com.xzcode.ggserver.core.common.filter.EventFilter;
import com.xzcode.ggserver.core.common.session.GGSession;
import com.xzcode.ggserver.core.common.utils.logger.GGLoggerUtil;
import com.xzcode.ggx.game.card.games.house.House;
import com.xzcode.ggx.game.card.games.player.RoomPlayer;
import com.xzcode.ggx.game.card.games.room.SingleThreadExecutorRoom;

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
public class SingleThreadExecutorRoomOperactionEventFilter
<
P extends RoomPlayer<P, R, H>,
R extends SingleThreadExecutorRoom<P, R, H>,
H extends House<P, R, H>
> implements EventFilter {
	
	protected EventManager eventManager;
	
	protected String playerSessionKey;

	public SingleThreadExecutorRoomOperactionEventFilter(String playerSessionKey, EventManager eventManager) {
		this.playerSessionKey = playerSessionKey;
		this.eventManager = eventManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean doFilter(EventData<?> eventData) {
		GGSession session = eventData.getSession();
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
					eventManager.emitEvent(eventData);
				} catch (Exception e) {
					GGLoggerUtil.getLogger(this).error("Event Filter Error!", e);
				}
			});
			return false;
		}
		return true;
	}

}
