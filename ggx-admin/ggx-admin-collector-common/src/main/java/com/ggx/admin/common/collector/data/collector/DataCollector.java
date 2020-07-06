package com.ggx.admin.common.collector.data.collector;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

/**
 * 信息收集器统一接口
 *
 * @param <T>
 * @author zai
 * 2020-04-21 15:57:37
 */
public interface DataCollector extends Runnable {

	/**
	 * 进行收集
	 *
	 * @author zai
	 * 2020-04-21 15:57:50
	 */
	void collect();
	
	/**
	 * 收集周期时间
	 *
	 * @return
	 * @author zai
	 * 2020-07-03 15:36:03
	 */
	long collectPeriodMs();
	
	
	default long collectInitDelayMs() {
		return 5000L;
	}
	
	
	@Override
	default void run() {
		collect();
	}
}