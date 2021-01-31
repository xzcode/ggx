package com.ggx.core.common.future;

import java.util.concurrent.Future;

import com.ggx.core.common.executor.TaskExecutor;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.session.manager.DefaultSessionManager;
import com.ggx.util.logger.GGXLogUtil;

/**
 * 未来对象
 * 
 * @param <V>
 * 
 * @author zai 2019-11-24 17:35:47
 */
public interface GGXFuture<T> extends Future<T> {

	/**
	 * 添加回调监听器
	 *
	 * @param listener 监听器
	 * @author zai
	 * 2020-11-11 15:36:40
	 */
	void addListener(GGXFutureListener<T> listener);
	
	/**
	 * 添加回调监听器
	 *
	 * @param executor 回调执行器
	 * @param listener 监听器
	 * 2021-01-31 22:19:38
	 */
	default void addListener(TaskExecutor executor, GGXFutureListener<T> listener) {
		if (executor != null) {
			this.addListener(f -> {
				executor.submitTask(() -> {
					try {
						listener.operationComplete(this);
					} catch (Exception e) {
						GGXLogUtil.getLogger(this).error("GGXFuture trigger listener ERROR!", e);
					}
				});
			});
		}
	};

	/**
	 * 取消该任务(针对定时任务)
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:32:18
	 */
	boolean cancel();

	/**
	 * 等待直到返回结果
	 *
	 * @param timeout 等待超时时间
	 * @return
	 * @author zai
	 * 2020-11-11 15:30:35
	 */
	T get(long timeout);

	/**
	 * 等待直到返回结果(默认 30s)
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:30:35
	 */
	T get();

	/**
	 * 是否操作成功
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:36:54
	 */
	boolean isSuccess();

	/**
	 * 获取会话(如果可以)
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:37:09
	 */
	GGXSession getSession();

	/**
	 * 异步任务中抛出的异常对象
	 *
	 * @return
	 * @author zai
	 * 2020-11-11 15:37:38
	 */
	Throwable cause();

}