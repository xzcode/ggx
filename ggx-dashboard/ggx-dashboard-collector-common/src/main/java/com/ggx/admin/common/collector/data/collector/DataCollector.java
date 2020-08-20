package com.ggx.admin.common.collector.data.collector;

/**
 * 信息收集器统一接口
 *
 * @param <T>
 * @author zai
 * 2020-04-21 15:57:37
 */
public interface DataCollector<T> extends Runnable {

	/**
	 * 进行收集
	 *
	 * @author zai
	 * 2020-04-21 15:57:50
	 */
	T collect();
	
	
	@Override
	default void run() {
		collect();
	}
}
