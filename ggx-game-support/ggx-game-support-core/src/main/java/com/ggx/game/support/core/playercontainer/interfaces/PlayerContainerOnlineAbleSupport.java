package com.ggx.game.support.core.playercontainer.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ggx.game.support.core.interfaces.condition.CheckCondition;
import com.ggx.game.support.core.player.support.OnlineAble;

/**
 * 房间游戏控制器接口
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-02-16 17:57:18
 * @param <R>
 */
public interface PlayerContainerOnlineAbleSupport<P extends OnlineAble> {
	
	
	
	/**
	 * 获取所有玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:18:49
	 */
	Map<String, P> getPlayers();

	/**
	 * 根据条件获取玩家
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-20 19:01:58
	 */
	default List<P> getPlayers(CheckCondition<P> condition) {
			Map<String, P> players = getPlayers();
			List<P> pList = new ArrayList<>(players.size());
			for (Entry<String, P> entry : players.entrySet()) {
				if (condition == null) {
					pList.add(entry.getValue());
				}else {
					if (condition.check(entry.getValue())) {
						pList.add(entry.getValue());
					}			
				}
			}
			return pList;
	}


	/**
	 * 获取已排序的参在线的玩家
	 * 
	 * @param room
	 * @param comparator
	 * @return
	 * @author zai
	 * 2019-05-30 12:17:29
	 */
	default List<P> getSortedOnlinePlayerList(Comparator<P> comparator) {
			List<P> onlinePlayers = getPlayers(player -> {
				return player.isOnline();
			});
			Collections.sort(onlinePlayers, comparator);
			return onlinePlayers;
	}

	/**
	 * 遍历每个已准备玩家
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zai
	 * 2019-05-16 14:19:58
	 */
	default void eachOnlinePlayer(ForEachPlayer<P> eachPlayer) {
		P player = null;
		for (Entry<String, P> e : getPlayers().entrySet()) {
			player = e.getValue();
			if (player.isOnline()) {
				eachPlayer.each(e.getValue());				
			}
		}
	}
	
	/**
	 * 获取指定条件的参在线的玩家数量
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-05-21 15:54:04
	 */
	default int countOnlinePlayers(CheckCondition<P> condition) {
		int i = 0;
		P player = null;
		for (Entry<String, P> entry : getPlayers().entrySet()) {
			player = entry.getValue();
			if (player.isOnline() && condition.check(player)) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * 获取参在线的玩家list
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-05-30 12:03:07
	 */
	default List<P> getOnlinePlayerList() {
		Map<String, P> players = getPlayers();
		List<P> pList = new ArrayList<>(players.size());
		for (Entry<String, P> entry : players.entrySet()) {
			P player = entry.getValue();
			if (player.isOnline()) {
				pList.add(player);					
			}
		}
		return pList;
	}


	/**
	 * 双循环玩家遍历
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zzz
	 * 2019-09-10 12:28:39
	 */
	default void doubleEachOnlinePlayer(DoubleEachPlayer<P> eachPlayer) {
		P player = null;
		P player2 = null;
		Set<Entry<String, P>> entrySet = getPlayers().entrySet();
		for (Entry<String, P> e : entrySet) {
			player = e.getValue();
			if (player.isOnline()) {
				for (Entry<String, P> e2 : entrySet) {
					player2 = e2.getValue();
					if (player2.isOnline()) {
						eachPlayer.each(player, player2);							
					}
				}
			}
		}
	}

	
}
