package com.ggx.core.common.executor.support;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.timeout.TimeoutTask;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXNettyFuture;

import io.netty.channel.EventLoopGroup;

/**
 * 计划任务执行支持接口
 * 
 * 
 * @author zai
 * 2019-12-01 16:15:52
 */
public interface ExecutorSupport extends TaskExecutor {
	
	/**
	 * 获取任务执行器
	 * 
	 * @return
	 * @author zai
	 * 2019-12-21 11:16:45
	 */
	TaskExecutor getTaskExecutor();

	@Override
	default GGXFuture submitTask(Runnable runnable) {
		return getTaskExecutor().submitTask(runnable);
	}

	@Override
	default <V> GGXFuture submitTask(Callable<V> callable) {
		return getTaskExecutor().submitTask(callable);
	}

	@Override
	default GGXFuture schedule(long delay, TimeUnit timeUnit, Runnable runnable) {
		return  getTaskExecutor().schedule(delay, timeUnit, runnable);
	}

	@Override
	default <V> GGXFuture schedule(long delay, TimeUnit timeUnit, Callable<V> callable) {
		return  getTaskExecutor().schedule(delay, timeUnit, callable);
	}

	@Override
	default GGXFuture scheduleAfter(GGXFuture afterFuture, long delay, TimeUnit timeUnit, Runnable runnable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delay, timeUnit, runnable);
	}
	
	@Override
	default GGXFuture scheduleAfter(GGXFuture afterFuture, long delayMs, Runnable runnable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delayMs, TimeUnit.MILLISECONDS, runnable);
	}

	@Override
	default <V> GGXFuture scheduleAfter(GGXFuture afterFuture, long delay, TimeUnit timeUnit,
			Callable<V> callable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delay, timeUnit, callable);
	}

	@Override
	default GGXFuture scheduleWithFixedDelay(long initialDelay, long delay, TimeUnit timeUnit, Runnable runnable) {
		return getTaskExecutor().scheduleWithFixedDelay(initialDelay, delay, timeUnit, runnable);
	}

	@Override
	default GGXFuture schedule(long delayMs, Runnable runnable) {
		return getTaskExecutor().schedule(delayMs, runnable);
	}

	@Override
	default TimeoutTask timeoutTask(long timeoutDelay, Runnable timeoutAction) {
		return getTaskExecutor().timeoutTask(timeoutDelay, timeoutAction);
	}

	@Override
	default TaskExecutor nextEvecutor() {
		return getTaskExecutor().nextEvecutor();
	}
	
	@Override
	default void execute(Runnable command) {
		getTaskExecutor().submitTask(command);
	}

	@Override
	default GGXFuture shutdown() {
		return new GGXNettyFuture(getEventLoopGroup().shutdownGracefully());
		
	}

	@Override
	default EventLoopGroup getEventLoopGroup() {
		return getTaskExecutor().getEventLoopGroup();
	}

	
	
}
