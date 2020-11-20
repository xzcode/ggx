package com.ggx.game.support.core.playercontainer.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.game.support.core.player.GGXPlayer;
import com.ggx.game.support.core.playercontainer.PlayerContainer;
import com.ggx.game.support.core.playercontainer.listener.PlayerEnterListener;
import com.ggx.game.support.core.playercontainer.listener.PlayerLeaveListener;

/**
 * 游戏场景基类
 *
 * @param <P> 玩家
 * @author zai
 * 2020-09-10 18:03:09
 */
public abstract class AbstractPlayerContainer<P extends GGXPlayer> implements PlayerContainer<P> {	
	
	
	/**
	 * 玩家集合
	 */
	protected Map<String, P> players = new ConcurrentHashMap<>(getMaxPlayerSize());
	
	/**
	 * 添加玩家监听器
	 */
	protected List<PlayerEnterListener<P>> playerEnterListeners = new CopyOnWriteArrayList<>();
	
	/**
	 * 移除玩家监听器
	 */
	protected List<PlayerLeaveListener<P>> playerLeaveListeners = new CopyOnWriteArrayList<>();
	
	
	/**
	 * 是否满员
	 * 
	 * @return
	 * @author zai
	 * 2019-02-20 20:24:18
	 */
	@Override
	public boolean isFullPlayers() {
		return players.size() >= this.getMaxPlayerSize();
	}
	
	
	/**
	 * 移除玩家
	 * 
	 * @param playerId
	 * @return
	 * @author zai
	 * 2019-02-20 19:10:19
	 */
	@Override
	public P removePlayer(P player) {
		P remove = players.remove(player.getPlayerId());
		if (remove != null) {
			if (player != null && playerLeaveListeners.size() > 0) {
				for (PlayerLeaveListener<P> listener : playerLeaveListeners) {
					listener.leave(player);
				}
			}
		}
		return remove;
	}
	
	/**
	 * 添加玩家
	 * 
	 * @param player
	 * @author zai 2019-01-24 19:42:15
	 */
	@Override
	public void addPlayer(P player) {
		if (player != null) {
			this.players.put(player.getPlayerId(), player);
			if (playerLeaveListeners.size() > 0) {
				for (PlayerEnterListener<P> listener : playerEnterListeners) {
					listener.enter(player);
				}
			}
		}
	}
	/**
	 * 添加监听添加玩家事件监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2019-04-13 15:31:37
	 */
	@Override
	public void addPlayerEnterListener(PlayerEnterListener<P> listener) {
		this.playerEnterListeners.add(listener);
	}
	/**
	 * 添加移除玩家监听器
	 * 
	 * @param listener
	 * @author zai
	 * 2019-04-13 15:32:03
	 */
	@Override
	public void addPlayerLeaveListener(PlayerLeaveListener<P> listener) {
		this.playerLeaveListeners.add(listener);
	}

	
	/**
	 * 获取当前玩家数
	 * 
	 * @return
	 * @author zai
	 * 2019-02-20 20:24:50
	 */
	@Override
	public int getCurrentPlayerSize() {
		return players.size();
	}
	
	
	
	@Override
	public Map<String, P> getPlayers() {
		return players;
	}

	@Override
	public P getPlayer(String playerId) {
		return players.get(playerId);
	}

	public void setPlayers(Map<String, P> players) {
		this.players = players;
	}

	public List<PlayerEnterListener<P>> getAfterAddPlayerListeners() {
		return playerEnterListeners;
	}


	public void setAfterAddPlayerListeners(List<PlayerEnterListener<P>> afterAddPlayerListeners) {
		this.playerEnterListeners = afterAddPlayerListeners;
	}


	public List<PlayerLeaveListener<P>> getAfterRemovePlayerListeners() {
		return playerLeaveListeners;
	}


	public void setAfterRemovePlayerListeners(List<PlayerLeaveListener<P>> afterRemovePlayerListeners) {
		this.playerLeaveListeners = afterRemovePlayerListeners;
	}

	
}

