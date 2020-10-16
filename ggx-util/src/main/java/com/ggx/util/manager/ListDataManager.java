package com.ggx.util.manager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * List管理器接口
 *
 * @param <T>
 * @author zai
 * 2020-10-16 14:19:29
 */
public interface ListDataManager<T> {
	
	
	
	List<T> getList();
	
	/**
	 * 添加
	 *
	 * @param obj
	 * @author zai
	 * 2020-10-16 14:19:24
	 */
	default void add(T obj) {
		getList().add(obj);
	}
	
	/**
	 * 获取
	 *
	 * @param index
	 * @return
	 * @author zai
	 * 2020-10-16 14:19:17
	 */
	default T get(int index) {
		return getList().get(index);
	}
	
	/**
	 * 移除
	 *
	 * @param index 下标 
	 * @return
	 * @author zai
	 * 2020-10-16 14:20:51
	 */
	default T remove(int index) {
		return getList().remove(index);
	}
	
	
	/**
	 * 
	 *移除
	 * @param obj 元素
	 * @return
	 * @author zai
	 * 2020-10-16 14:20:46
	 */
	default boolean remove(T obj) {
		return getList().remove(obj);
	}
	
	/**
	 * 随机获取一个
	 * @return
	 * @author zai
	 * 2020-10-2 14:35:04
	 */
	default T getRandomOne() {
		List<T> list = getList();
		if (list.size() == 1) {
			return list.get(0);
		}
		if (list.size() > 1) {
			return list.get(ThreadLocalRandom.current().nextInt(list.size()));
		}
		return null;
	}
	
	/**
	 * 获取map中元素个数
	 * @return
	 * @author zai
	 * 2020-10-3 15:19:43
	 */
	default int size() {
		return this.getList().size();
	}

}
