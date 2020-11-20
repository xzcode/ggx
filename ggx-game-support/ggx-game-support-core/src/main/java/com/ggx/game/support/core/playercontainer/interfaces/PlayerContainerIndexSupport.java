package com.ggx.game.support.core.playercontainer.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.send.support.SendMessageSupport;
import com.ggx.game.support.core.interfaces.condition.CheckCondition;
import com.ggx.game.support.core.player.GGXPlayer;
import com.ggx.game.support.core.player.support.IndexAble;

/**
 * 房间游戏控制器接口
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-02-16 17:57:18
 * @param <R>
 */
public interface PlayerContainerIndexSupport<P extends IndexAble>{
	
	
	
	/**
	 * 获取所有玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:18:49
	 */
	Map<Object, P> getPlayers();
	

	/**
	 * 获取一个序号
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-21 11:15:34
	 */
	default int getNewIndex() {
		
		int maxPlayerNum = getMaxPlayerSize();
		List<Integer> nums = new ArrayList<>(maxPlayerNum);
		for (int i = 1; i <= maxPlayerNum; i++) {
			nums.add(i);
		}
		Map<Object, P> players = getPlayers();
		for (Entry<Object, P> entry : players.entrySet()) {
			nums.remove(new Integer(entry.getValue().getIndex()));
		}
		if (nums.size() == 0) {
			return -1;
		}
		return nums.get(0);
	}

	/**
	 * 获取最大玩家数
	 * @return
	 * @author zai
	 * 2020-9-14 23:34:11
	 */
	int getMaxPlayerSize();

	
}
