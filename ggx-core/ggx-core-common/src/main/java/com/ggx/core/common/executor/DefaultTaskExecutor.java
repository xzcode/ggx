package com.ggx.core.common.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.ggx.core.common.executor.task.AsyncCallableTask;
import com.ggx.core.common.executor.task.AsyncRunnableTask;
import com.ggx.core.common.executor.thread.GGXThreadFactory;
import com.ggx.core.common.executor.timeout.TimeoutTask;
import com.ggx.core.common.future.GGXNettyFuture;
import com.ggx.core.common.future.GGXFuture;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.ScheduledFuture;

public class DefaultTaskExecutor implements TaskExecutor{
	
	protected EventLoopGroup executor;

	public DefaultTaskExecutor(EventLoopGroup executor) {
		this.executor = executor;
	}
	public DefaultTaskExecutor(String threadNamePrefix, int threadSize) {
		this.executor = new DefaultEventLoopGroup(threadSize, new GGXThreadFactory(threadNamePrefix, false));
	}
	public DefaultTaskExecutor(int threadSize) {
		this.executor = new DefaultEventLoopGroup(threadSize, new GGXThreadFactory("GGXTaskExecutor-", false));
	}

	@Override
	public GGXFuture submitTask(Runnable runnable) {
		return new GGXNettyFuture(executor.submit(new AsyncRunnableTask(runnable)));
	}

	@Override
	public <V> GGXFuture submitTask(Callable<V> callable) {
		return new GGXNettyFuture(executor.submit(new AsyncCallableTask<>(callable)));
	}
	@Override
	public GGXFuture schedule(long delay, TimeUnit timeUnit, Runnable runnable) {
		return new GGXNettyFuture(executor.schedule(new AsyncRunnableTask(runnable), delay, timeUnit));
	}

	@Override
	public <V> GGXFuture schedule(long delay, TimeUnit timeUnit, Callable<V> callable) {
		return new GGXNettyFuture(executor.schedule(new AsyncCallableTask<>(callable), delay, timeUnit));
	}

	@Override
	public GGXFuture scheduleAfter(GGXFuture afterFuture, long delay, TimeUnit timeUnit, Runnable runnable) {
		GGXNettyFuture taskFuture = new GGXNettyFuture();
		afterFuture.addListener(f -> {
			AsyncRunnableTask asyncTask = new AsyncRunnableTask(runnable);
			ScheduledFuture<?> future = executor.schedule(asyncTask, delay, timeUnit);
			taskFuture.setFuture(future);
		});
		return taskFuture;
	}
	

	@Override
	public GGXFuture scheduleAfter(GGXFuture afterFuture, long delayMs, Runnable runnable) {
		return scheduleAfter(afterFuture, delayMs, TimeUnit.MILLISECONDS, runnable);
	}

	@Override
	public <V> GGXFuture scheduleAfter(GGXFuture afterFuture, long delay, TimeUnit timeUnit, Callable<V> callable) {
		GGXNettyFuture taskFuture = new GGXNettyFuture();
		afterFuture.addListener(f -> {
			AsyncCallableTask<V> asyncTask = new AsyncCallableTask<>(callable);
			ScheduledFuture<?> future = executor.schedule(asyncTask, delay, timeUnit);
			taskFuture.setFuture(future);
		});
		return taskFuture;
	}

	@Override
	public GGXFuture scheduleWithFixedDelay(long initialDelay, long delay, TimeUnit timeUnit, Runnable runnable) {
		return new GGXNettyFuture(executor.scheduleWithFixedDelay(new AsyncRunnableTask(runnable), initialDelay, delay, timeUnit));
	}

	@Override
	public GGXFuture schedule(long delayMs, Runnable runnable) {
		return schedule(delayMs, TimeUnit.MILLISECONDS, runnable);
	}

	@Override
	public TaskExecutor nextEvecutor() {
		return new DefaultTaskExecutor(executor.next());
	}
	@Override
	public TimeoutTask timeoutTask(long timeoutDelay, Runnable timeoutAction) {
		return new TimeoutTask(this, timeoutDelay, timeoutAction);
	}
	@Override
	public void execute(Runnable command) {
		submitTask(command);
	}
	@Override
	public GGXFuture shutdown() {
		return new GGXNettyFuture(this.executor.shutdownGracefully());
	}
	@Override
	public EventLoopGroup getEventLoopGroup() {
		return this.executor;
	}



}
