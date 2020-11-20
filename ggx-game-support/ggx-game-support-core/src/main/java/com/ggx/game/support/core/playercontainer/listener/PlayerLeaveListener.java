package com.ggx.game.support.core.playercontainer.listener;

/**
 * 移除玩家监听器
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-07-08 10:26:00
 */
@FunctionalInterface
public interface PlayerLeaveListener<P> {
	
	/**
	 * 离开行为
	 *
	 * @param player
	 * @author zai
	 * 2020-09-10 18:07:15
	 */
	void leave(P player);
	
}
