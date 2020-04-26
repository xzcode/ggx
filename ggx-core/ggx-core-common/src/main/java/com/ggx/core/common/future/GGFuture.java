package com.ggx.core.common.future;

import java.util.concurrent.Future;

import com.ggx.core.common.session.GGSession;

/**
 * 未来对象
 * @param <V>
 * 
 * @author zai
 * 2019-11-24 17:35:47
 */
public interface GGFuture extends Future<Object>{


	void addListener(IGGFutureListener<GGFuture> listener);
	
	boolean cancel();
	
	<T> T get(Class<T> clazz);

	boolean isSuccess();
	
	GGSession getSession();

}