package com.ggx.util.wheeltimer;

import io.netty.util.Timeout;

/**
 * 延迟任务future操作
 *
 * 2020-12-19 19:50:08
 */
public class GGXWheelTimerTaskFuture {
	
	private Timeout timeout;
	private long endTime;

	public GGXWheelTimerTaskFuture(Timeout timeout, long delay, long startTime) {
		this.timeout = timeout;
		this.endTime = startTime + delay;
	}
	
	/**
	 * 获取剩余时间
	 *
	 * @return
	 * 2020-12-24 11:46:57
	 */
	public long getRemainingTime() {
		long remaining = this.endTime - System.currentTimeMillis();
		if (remaining < 0) {
			remaining = 0;
		}
		return remaining;
	}

	/**
	 * 是否已超时
	 *
	 * @return
	 * 2020-12-19 19:50:26
	 */
	public boolean isExpired() {
		return timeout.isExpired();
	}
	
	/**
	 * 是否已取消
	 *
	 * @return
	 * 2020-12-19 19:50:33
	 */
	public boolean isCancelled() {
		return timeout.isCancelled();
	}

	/**
	 * 取消任务
	 *
	 * @return 返回true则取消成功，flase则执行中或已执行完毕
	 * 2020-12-19 19:50:43
	 */
	public boolean cancel() {
		return timeout.cancel();
		
	}

}
