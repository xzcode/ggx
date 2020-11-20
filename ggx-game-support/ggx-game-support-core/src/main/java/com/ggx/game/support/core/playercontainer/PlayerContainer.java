package com.ggx.game.support.core.playercontainer;

import java.util.Map;

import com.ggx.game.support.core.playercontainer.listener.PlayerEnterListener;
import com.ggx.game.support.core.playercontainer.listener.PlayerLeaveListener;

/**
 * GGX场景统一接口
 *
 * @param <P>
 * @author zai
 * 2020-09-10 18:22:50
 */
public interface PlayerContainer<P> {

	/**
	 * 获取最大玩家数
	 * 
	 * @return
	 * @author zai
	 * 2019-02-20 19:07:54
	 */
	int getMaxPlayerSize();

	/**
	 * 是否满员
	 * 
	 * @return
	 * @author zai
	 * 2019-02-20 20:24:18
	 */
	boolean isFullPlayers();

	/**
	 * 移除玩家
	 * 
	 * @param playerId
	 * @return
	 * @author zai
	 * 2019-02-20 19:10:19
	 */
	P removePlayer(P player);

	/**
	 * 添加玩家
	 * 
	 * @param player
	 * @author zai 2019-01-24 19:42:15
	 */
	void addPlayer(P player);

	/**
	 * 添加监听添加玩家事件监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2019-04-13 15:31:37
	 */
	void addPlayerEnterListener(PlayerEnterListener<P> listener);

	/**
	 * 添加移除玩家监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2019-04-13 15:32:03
	 */
	void addPlayerLeaveListener(PlayerLeaveListener<P> listener);

	/**
	 * 获取当前玩家数
	 * 
	 * @return
	 * @author zai
	 * 2019-02-20 20:24:50
	 */
	int getCurrentPlayerSize();

	/**
	 * 获取玩家map集合
	 * @return
	 * @author zai
	 * 2020-9-14 22:09:10
	 */
	Map<String, P> getPlayers();

	/**
	 * 获取指定玩家
	 * @param playerId
	 * @return
	 * @author zai
	 * 2020-9-14 22:09:25
	 */
	P getPlayer(String playerId);

}