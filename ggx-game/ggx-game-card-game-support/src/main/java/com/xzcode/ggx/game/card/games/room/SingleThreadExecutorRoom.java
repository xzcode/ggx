package com.xzcode.ggx.game.card.games.room;

import java.nio.charset.Charset;

import com.xzcode.ggserver.core.common.executor.TaskExecutor;
import com.xzcode.ggserver.core.common.executor.support.IExecutorSupport;
import com.xzcode.ggserver.core.common.filter.FilterManager;
import com.xzcode.ggserver.core.common.handler.serializer.ISerializer;
import com.xzcode.ggserver.core.common.session.manager.ISessionManager;
import com.xzcode.ggx.game.card.games.house.House;
import com.xzcode.ggx.game.card.games.player.RoomPlayer;
import com.xzcode.ggx.game.card.games.room.support.IRoomSupport;

/**
 * 单线程执行器房间
 * 
 * @param <P>
 * @param <R>
 * @param <H>
 * @author zai
 * 2019-12-22 17:32:54
 */
public abstract class SingleThreadExecutorRoom
<
P extends RoomPlayer<P, R, H>,
R extends Room<P, R, H>, 
H extends House<P, R, H>
>
extends Room<P, R, H>
implements 
IRoomSupport<P, R, H>,
IExecutorSupport
{	
	
	/**
	 * 单线程执行器
	 */
	protected TaskExecutor taskExecutor;

	public SingleThreadExecutorRoom(TaskExecutor singleThreadExecutor) {
		this.taskExecutor = singleThreadExecutor;
	}
	
	/**
	 * 添加操作到待执行队列
	 * 
	 * @param oper
	 * @author zai
	 * 2019-12-22 17:33:57
	 */
	@SuppressWarnings("unchecked")
	public void addOperaction(IRoomOperaction<R> oper) {
		taskExecutor.submitTask(() -> { oper.oper((R) this);});
	}
	

	@Override
	public TaskExecutor getTaskExecutor() {
		//此处使用房间内的任务执行器
		return this.taskExecutor;
	}

	@Override
	public Charset getCharset() {
		return getGGserver().getCharset();
	}

	@Override
	public ISerializer getSerializer() {
		return getGGserver().getSerializer();
	}

	@Override
	public ISessionManager getSessionManager() {
		return getGGserver().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return getGGserver().getFilterManager();
	}

	
	
}

