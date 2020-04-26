package com.ggx.game.card.game.support.player.factory;

/**
 * 玩家工厂接口
 * 
 * @param <P>
 * @author zai
 * 2019-12-26 17:28:10
 */
public interface PlayerFactory<P> {

	/**
	 * 创建玩家
	 * 
	 * @return
	 * @author zai
	 * 2019-12-26 17:28:17
	 */
	P createPlayer();

}
