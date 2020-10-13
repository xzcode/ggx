package com.ggx.core.common.future;

import java.util.concurrent.Future;

import com.ggx.core.common.session.GGXSession;

/**
 * 未来对象
 * @param <V>
 * 
 * @author zai
 * 2019-11-24 17:35:47
 */
public interface GGXFuture extends Future<Object>{


	void addListener(GGXFutureListener<GGXFuture> listener);
	
	boolean cancel();
	
	<T> T get(Class<T> clazz);

	boolean isSuccess();
	
	GGXSession getSession();
	
	Throwable cause();

}