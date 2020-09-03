package com.ggx.game.card.game.support.room;

import java.nio.charset.Charset;

import com.ggx.core.common.config.GGXCore;
import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.filter.FilterManager;
import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.session.manager.SessionManager;
import com.ggx.game.card.game.support.house.House;
import com.ggx.game.card.game.support.player.RoomPlayer;
import com.ggx.game.card.game.support.room.support.RoomSupport;

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
RoomSupport<P, R, H>,
ExecutorSupport
{	
	
	/**
	 * 单线程执行器
	 */
	protected GGXCore core;
	
	protected TaskExecutor taskExecutor;

	public SingleThreadExecutorRoom(GGXCore core) {
		this.core = core;
		this.taskExecutor = core.nextEvecutor();
	}
	
	/**
	 * 添加操作到待执行队列
	 * 
	 * @param oper
	 * @author zai
	 * 2019-12-22 17:33:57
	 */
	@SuppressWarnings("unchecked")
	public void addOperaction(RoomOperaction<R> oper) {
		taskExecutor.submitTask(() -> { oper.oper((R) this);});
	}
	

	@Override
	public TaskExecutor getTaskExecutor() {
		//此处使用房间内的任务执行器
		return this.taskExecutor;
	}

	@Override
	public Charset getCharset() {
		return getCore().getCharset();
	}

	@Override
	public Serializer getSerializer() {
		return getCore().getSerializer();
	}

	@Override
	public SessionManager getSessionManager() {
		return getCore().getSessionManager();
	}

	@Override
	public FilterManager getFilterManager() {
		return getCore().getFilterManager();
	}

	public GGXCore getCore() {
		return core;
	}
	
	
	
}

