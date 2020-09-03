package com.ggx.core.common.future;

import java.util.EventListener;

/**
 * 未来事件监听器
 * @param <F>
 * 
 * @author zai
 * 2019-11-24 17:49:22
 */
public interface GGXFutureListener<F extends GGFuture> extends EventListener{

	/**
	 * 操作完成调用
	 * @param future
	 * @throws Exception
	 * 
	 * @author zai
	 * 2019-11-24 17:49:40
	 */
	void operationComplete(F future) throws Exception;

}