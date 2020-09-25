package com.ggx.core.common.executor.support;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.executor.timeout.TimeoutTask;
import com.ggx.core.common.future.GGFuture;

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
	default GGFuture submitTask(Runnable runnable) {
		return getTaskExecutor().submitTask(runnable);
	}

	@Override
	default <V> GGFuture submitTask(Callable<V> callable) {
		return getTaskExecutor().submitTask(callable);
	}

	@Override
	default GGFuture schedule(long delay, TimeUnit timeUnit, Runnable runnable) {
		return  getTaskExecutor().schedule(delay, timeUnit, runnable);
	}

	@Override
	default <V> GGFuture schedule(long delay, TimeUnit timeUnit, Callable<V> callable) {
		return  getTaskExecutor().schedule(delay, timeUnit, callable);
	}

	@Override
	default GGFuture scheduleAfter(GGFuture afterFuture, long delay, TimeUnit timeUnit, Runnable runnable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delay, timeUnit, runnable);
	}
	
	@Override
	default GGFuture scheduleAfter(GGFuture afterFuture, long delayMs, Runnable runnable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delayMs, TimeUnit.MILLISECONDS, runnable);
	}

	@Override
	default <V> GGFuture scheduleAfter(GGFuture afterFuture, long delay, TimeUnit timeUnit,
			Callable<V> callable) {
		return  getTaskExecutor().scheduleAfter(afterFuture, delay, timeUnit, callable);
	}

	@Override
	default GGFuture scheduleWithFixedDelay(long initialDelay, long delay, TimeUnit timeUnit, Runnable runnable) {
		return getTaskExecutor().scheduleWithFixedDelay(initialDelay, delay, timeUnit, runnable);
	}

	@Override
	default GGFuture schedule(long delayMs, Runnable runnable) {
		return getTaskExecutor().schedule(delayMs, runnable);
	}

	@Override
	default TimeoutTask timeoutTask(long timeoutDelay, Runnable timeoutAction) {
		return getTaskExecutor().timeoutTask(timeoutDelay, timeoutAction);
	}

	@Override
	default TaskExecutor nextExecutor() {
		return getTaskExecutor().nextExecutor();
	}
	
	@Override
	default void execute(Runnable command) {
		getTaskExecutor().submitTask(command);
	}
	
}
