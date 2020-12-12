package com.ggx.util.future;

/**
 * future对象
 *
 * @param <T>
 * 2020-12-12 15:51:00
 */
public interface Future<T> extends java.util.concurrent.Future<T> {

	/**
	 * 添加回调监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-11-11 15:36:40
	 */
	void addListener(FutureListener<T> listener);

	/**
	 * 取消该任务(针对定时任务)
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:32:18
	 */
	boolean cancel();

	/**
	 * 等待直到返回结果
	 *
	 * @param timeout 等待超时时间
	 * @return
	 * @author zai
	 * 2020-11-11 15:30:35
	 */
	T get(long timeout);

	/**
	 * 等待直到返回结果(默认 30s)
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:30:35
	 */
	T get();

	/**
	 * 是否操作成功
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:36:54
	 */
	boolean isSuccess();

	/**
	 * 异步任务中抛出的异常对象
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:37:38
	 */
	Throwable cause();

}