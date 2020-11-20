package com.ggx.game.support.core.playercontainer.listener;

/**
 * 添加玩家监听器
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-07-08 10:26:15
 */
@FunctionalInterface
public interface PlayerEnterListener<P> {
	
	/**
	 * 玩家进入
	 *
	 * @param player
	 * @author zai
	 * 2020-09-10 18:06:40
	 */
	void enter(P player);
	
}
