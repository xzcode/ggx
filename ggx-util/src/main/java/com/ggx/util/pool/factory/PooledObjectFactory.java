package com.ggx.util.pool.factory;

public interface PooledObjectFactory<T> {
	
	T createObject();
	
}
