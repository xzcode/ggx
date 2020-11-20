package com.ggx.game.support.core.playercontainer.interfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.send.support.SendMessageSupport;
import com.ggx.game.support.core.interfaces.condition.CheckCondition;
import com.ggx.game.support.core.player.GGXPlayer;

/**
 * 房间游戏控制器接口
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-02-16 17:57:18
 * @param <R>
 */
public interface PlayerContainerSupport<P extends GGXPlayer> extends ExecutorSupport, SendMessageSupport {
	
	
	
	/**
	 * 获取所有玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:18:49
	 */
	Map<Object, P> getPlayers();
	

	/**
	 * 根据id获取玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:34:15
	 */
	default P getPlayer(String playerId) {
		return getPlayers().get(playerId);
	}
	
	/**
	 * 根据条件获取玩家
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-16 17:48:42
	 */
	default P getPlayer(CheckCondition<P> condition) {
		for (Entry<Object, P> entry : getPlayers().entrySet()) {
			if (condition.check(entry.getValue())) {
				return entry.getValue();
			}
		}
		return null;
	}
	

	/**
	 * 遍历所有玩家
	 * 
	 * @param room
	 * @param eachPlayer
	 * 
	 * @author zai 2019-02-10 14:19:14
	 */
	default void eachPlayer(ForEachPlayer<P> eachPlayer) {
		for (Entry<Object, P> e : getPlayers().entrySet()) {
			eachPlayer.each(e.getValue());
		}
	}
	
	/**
	 * 遍历所有玩家并返回布尔值
	 * 
	 * @param room
	 * @param eachPlayer
	 * @return
	 * @author zai
	 * 2019-02-11 10:56:05
	 */
	default boolean boolEachPlayer(BoolForEachPlayer<P> eachPlayer) {
		for (Entry<Object, P> entry : getPlayers().entrySet()) {
			if (!eachPlayer.each(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 广播给所有玩家
	 * 
	 * @param message
	 * @author zai
	 * 2019-12-25 18:52:39
	 */
	default void broadcastToAllPlayer(Message message) {
		eachPlayer((player) -> {
			player.send(message);
		});
	}
	
	

	/**
	 * 广播给所有满足条件的玩家
	 * @param room
	 * @param actionId
	 * @param message
	 * @param condition
	 * 
	 * @author zai
	 * 2019-02-23 16:49:17
	 */
	default void broadcastToAllPlayer(Message message, CheckCondition<P> condition) {
		eachPlayer((player) -> {
			if (condition.check(player)) {
				player.send(message);	
			}
		});
	}

	
	/**
	 * 随机获取玩家
	 * 
	 * @return
	 * @author zai
	 * 2019-02-16 17:59:11
	 */
	@SuppressWarnings("unchecked")
	default P getRandomPlayer() {
		Map<Object, P> players = getPlayers();
		if (players.size() <= 0) {
			return null;
		}
		if (players.size() == 1) {
			return players.entrySet().stream().findFirst().get().getValue();
		}
		return ((Entry<Object, P>)players.entrySet().toArray()[ThreadLocalRandom.current().nextInt(players.size())]).getValue();
	}

	/**
	 * 随机获取满足条件的玩家
	 * 
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-16 18:03:09
	 */
	default P getRandomPlayer(CheckCondition<P> condition) {
			Map<Object, P> players = getPlayers();
			List<P> plist = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
				if (condition.check(entry.getValue())) {
					plist.add(entry.getValue());
				}
			}
			if (players.size() > 1) {
				return plist.get(ThreadLocalRandom.current().nextInt(plist.size()));
			}
			return plist.get(0);
		
	}

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
			Map<Object, P> players = getPlayers();
			List<P> pList = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
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
	 * 获取玩家列表
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-20 20:36:06
	 */
	default List<P> getPlayerList() {
			Map<Object, P> players = getPlayers();
			List<P> pList = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
				pList.add(entry.getValue());
			}
			return pList;
	}
	
	/**
	 * 玩家迭代
	 * @param room
	 * @param iteration
	 * 
	 * @author zai
	 * 2019-02-23 16:03:00
	 */
	default void iteratePlayer(PlayerIteration<P> iteration) {
		Iterator<Entry<Object, P>> it = getPlayers().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, P> next = it.next();
			iteration.it(next.getValue(), next, it);			
		}
	}

	/**
	 * 获取指定条件的玩家数量
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-28 15:21:05
	 */
	default int countPlayers(CheckCondition<P> condition) {
			int i = 0;
			for (Entry<Object, P> entry : getPlayers().entrySet()) {
				if (condition.check(entry.getValue())) {
					i++;
				}
			}
			return i;
	}
	
	
	/**
	 * 获取玩家数量
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-28 15:22:16
	 */
	default int countPlayers() {
		return getPlayers().size();
	}


	
	/**
	 * 广播给所有玩家
	 * 
	 * @param room
	 * @param actionId
	 * @author zai
	 * 2019-06-26 11:06:07
	 */
	default void broadcastToAllPlayer(String actionId) {
		eachPlayer(player -> {
			player.send(actionId);
		});
	}
	
	/**
	 * 广播给所有玩家
	 * 
	 * @param actionId
	 * @param message
	 * @author zai
	 * 2019-12-26 10:03:47
	 */
	default void broadcastToAllPlayer(String actionId, Object message) {
		eachPlayer(player -> {
			player.send((Message) message);
		});
	}

	
	/**
	 * 双循环玩家遍历
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zzz
	 * 2019-09-10 12:16:44
	 */
	default void doubleEachPlayer(DoubleEachPlayer<P> eachPlayer) {
		Set<Entry<Object, P>> entrySet = getPlayers().entrySet();
		for (Entry<Object, P> e : entrySet) {
			for (Entry<Object, P> e2 : entrySet) {
				eachPlayer.each(e.getValue(), e2.getValue());
			}
		}
	}

	
}
