package com.ggx.core.common.executor;

import java.util.concurrent.ThreadFactory;

import com.ggx.util.thread.GGXThreadFactory;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;

/**
 * 单线程任务执行器
 * 
 * @author zai
 * 2019-12-24 17:45:30
 */
public class SingleThreadTaskExecutor extends DefaultTaskExecutor{
	
	public SingleThreadTaskExecutor(EventLoop eventLoop) {
		super(eventLoop);
	}
	
	public SingleThreadTaskExecutor() {
		super(new DefaultEventLoop());
	}

	public SingleThreadTaskExecutor(String threadNamePrefix) {
		super(new DefaultEventLoop(new GGXThreadFactory(threadNamePrefix, false)));
	}

	public SingleThreadTaskExecutor(ThreadFactory threadFactory) {
		super(new DefaultEventLoop(threadFactory));
	}

	@Override
	public TaskExecutor nextEvecutor() {
		return this;
	}


}
