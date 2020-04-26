package com.ggx.game.card.game.support.room.factory;

/**
 * 房间工厂
 * 
 * @param <R>
 * @author zai
 * 2019-12-25 19:57:56
 */
public interface RoomFactory<R> {
	
	/**
	 * 创建房间
	 * 
	 * @return
	 * @author zai
	 * 2019-12-25 19:58:03
	 */
	R createRoom();
}
