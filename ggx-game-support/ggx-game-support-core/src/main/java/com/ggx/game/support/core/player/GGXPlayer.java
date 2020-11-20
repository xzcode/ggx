package com.ggx.game.support.core.player;

import com.ggx.game.support.core.player.support.GGXPlayerSupport;

/**
 * GGX玩家统一接口
 *
 * @author zai
 * 2020-09-10 18:11:50
 */
public interface GGXPlayer extends GGXPlayerSupport{
	
	/**
	 * 获取玩家id
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 18:05:40
	 */
	String getPlayerId();

}
