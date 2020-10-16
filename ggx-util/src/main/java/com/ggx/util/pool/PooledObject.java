package com.ggx.util.pool;

/**
 * GGX池化对象接口
 *
 * @author zai
 * 2020-09-10 17:10:06
 */
public interface PooledObject {
	
	/**
	 * 对象归还前操作
	 *
	 * @author zai
	 * 2020-09-10 17:13:01
	 */
	void beforeReturn();
	
	/**
	 * 归还到对象池
	 *
	 * @author zai
	 * 2020-09-10 18:19:38
	 */
	default void returnToPool() {
		getObjectPool().returnObject(this);
	}
	
	/**
	 * 获取对象池
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 18:25:52
	 */
	ObjectPool<?> getObjectPool();

}
