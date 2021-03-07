package com.ggx.util.manager.list;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.ggx.util.interfaces.ForEach;

/**
 * List管理器接口
 *
 * @param <T>
 * @author zai
 * 2020-10-16 14:19:29
 */
public interface ListDataManager<T> {
	
	
	/**
	 * 获取集合
	 *
	 * @return
	 * 2020-11-27 18:43:27
	 */
	List<T> getList();
	
	/**
	 * 获取克隆集合
	 *
	 * @return
	 * 2020-11-27 18:43:19
	 */
	List<T> getCloneList();
	
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
	 * 获取元素个数
	 * @return
	 * @author zai
	 * 2020-10-3 15:19:43
	 */
	default int size() {
		return this.getList().size();
	}
	
	/**
	 * foreach遍历
	 *
	 * @param one
	 * 2020-11-30 18:08:45
	 */
	default void ForEach(ForEach<T> one) {
		this.getList().forEach(e -> one.each(e));
	}

}
