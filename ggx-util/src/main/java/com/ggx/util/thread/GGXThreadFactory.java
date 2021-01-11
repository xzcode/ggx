package com.ggx.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.util.concurrent.FastThreadLocalThread;

public class GGXThreadFactory implements ThreadFactory {
    private final String threadNamePrefix;
    private final AtomicInteger THREAD_IDX = new AtomicInteger();
    private final boolean daemon;

    public GGXThreadFactory(final String threadNamePrefix, final boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = new FastThreadLocalThread(r, threadNamePrefix + THREAD_IDX.getAndIncrement());
        t.setDaemon(daemon);
        return t;
    }
}
