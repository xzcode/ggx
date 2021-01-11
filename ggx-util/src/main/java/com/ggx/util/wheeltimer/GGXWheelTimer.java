package com.ggx.util.wheeltimer;

import java.util.concurrent.TimeUnit;

import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.thread.GGXThreadFactory;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

/**
 * 延迟任务管理器
 *
 * 2020-12-19 19:33:04
 */
public class GGXWheelTimer {
	
	private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
	
	private long tickDuration = 1000L;
	
	private int ticksPerWheel  = 60;

	/**
	 * netty哈希时间轮计时器
	 */
	private HashedWheelTimer wheelTimer;

	public GGXWheelTimer() {
		this.init();
	}
	
	

	public GGXWheelTimer(long tickDuration, int ticksPerWheel) {
		super();
		this.tickDuration = tickDuration;
		this.ticksPerWheel = ticksPerWheel;
	}



	/**
	 * 初始化时间轮管理器
	 *
	 * 2020-12-19 19:32:49
	 */
	private void init() {
		this.wheelTimer = new HashedWheelTimer(new GGXThreadFactory("wheel-timer-", false), tickDuration, TIME_UNIT, ticksPerWheel);
		this.wheelTimer.start();
	}
	
	/**
	 * 添加任务
	 *
	 * @param task 任务
	 * @param delay 延迟时长(毫秒)
	 * 2020-12-19 19:30:10
	 */
	public GGXWheelTimerTaskFuture addTask(long delay, Runnable task) {
		if (task == null) {
			throw new NullPointerException("Parameter [task] must not be null!");
		}
		Timeout timeout = this.wheelTimer.newTimeout(new TimerTask() {
			
			@Override
			public void run(Timeout timeout) throws Exception {
				try {
					task.run();
				} catch (Exception e) {
					GGXLogUtil.getLogger(this).error("Wheel timer task error!", e);
				}
				
			}
		}, delay, TimeUnit.MILLISECONDS);
		return new GGXWheelTimerTaskFuture(timeout, delay, System.currentTimeMillis());

	}
	
	/**
	 * 启动
	 *
	 * 2020-12-19 19:32:28
	 */
	public void start() {
		this.wheelTimer.start();
	}
	
	/**
	 * 停止
	 *
	 * 2020-12-19 19:32:40
	 */
	public void stop() {
		this.wheelTimer.stop();
	}

}
