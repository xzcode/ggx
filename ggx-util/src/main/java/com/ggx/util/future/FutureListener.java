package com.ggx.util.future;

import java.util.EventListener;

/**
 * 事件监听器
 * 
 * @param <F>
 * 
 * @author zai 2019-11-24 17:49:22
 */
public interface FutureListener<T> extends EventListener {

	/**
	 * 操作完成调用
	 * 
	 * @param future
	 * @throws Exception
	 * 
	 * @author zai 2019-11-24 17:49:40
	 */
	void operationComplete(Future<T> future) throws Exception;

}