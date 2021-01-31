package com.ggx.core.common.future;

/**
 * 未来事件监听器
 * 
 * @param <F>
 * 
 * @author zai 2019-11-24 17:49:22
 */
public interface GGXFutureListener <T> {
	
	void operationComplete(GGXFuture<T> future) throws Exception;
	
}