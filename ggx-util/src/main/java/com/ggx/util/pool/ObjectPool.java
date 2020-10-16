package com.ggx.util.pool;

import java.util.Queue;

import com.ggx.util.pool.factory.PooledObjectFactory;

/**
 * GGX对象池
 *
 * @param <T>
 * @author zai
 * 2020-09-10 17:07:28
 */
public interface ObjectPool<T extends PooledObject> {
	
	/**
	 * 借出对象
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 17:07:02
	 */
	T borrowObject();
	
	/**
	 * 归还对象
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 17:07:16
	 */
	@SuppressWarnings("unchecked")
	default void returnObject(PooledObject object) {
		object.beforeReturn();
		getObjectQueue().add((T) object);
	}
	
	/**
	 * 获取池化对象工厂
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 18:32:52
	 */
	PooledObjectFactory<T> getObjectFactory();
	
	/**
	 * 获取池化对象队列
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 18:34:40
	 */
	Queue<T> getObjectQueue();
	
}
