package com.ggx.util.manager.listener;

/**
 * 取消监听器
 *
 * @param <T>
 * @author zai
 * 2020-09-15 16:53:56
 */
public interface Listener<T> {
	
	/**
	 * 取消动作
	 *
	 * @param data
	 * @author zai
	 * 2020-09-15 16:54:12
	 */
	void onTrigger(T data);

}
