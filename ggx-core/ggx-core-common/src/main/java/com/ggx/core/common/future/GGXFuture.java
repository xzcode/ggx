package com.ggx.core.common.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ggx.core.common.session.GGXSession;

/**
 * 未来对象
 * @param <V>
 * 
 * @author zai
 * 2019-11-24 17:35:47
 */
public interface GGXFuture<T> extends Future<T>{


	void addListener(GGXFutureListener<T> listener);
	
	boolean cancel();
	
	
	@Deprecated
	@Override
	T get(long timeout, TimeUnit unit);

	T get();
	
	boolean isSuccess();
	
	GGXSession getSession();
	
	Throwable cause();

}