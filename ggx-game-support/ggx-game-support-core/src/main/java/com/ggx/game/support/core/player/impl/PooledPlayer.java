package com.ggx.game.support.core.player.impl;

import com.ggx.util.pool.ObjectPool;
import com.ggx.util.pool.PooledObject;

/**
 * 池化玩家基类
 *
 * @author zai
 * 2020-09-10 18:17:11
 */
public abstract class PooledPlayer extends AbstractPlayer implements PooledObject{
	
	protected ObjectPool<?> objectPool;

	public PooledPlayer(ObjectPool<?> objectPool) {
		this.objectPool = objectPool;
	} 
	
}
