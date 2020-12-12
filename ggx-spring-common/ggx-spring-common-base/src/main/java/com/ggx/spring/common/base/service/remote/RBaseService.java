package com.ggx.spring.common.base.service.remote;

import com.ggx.util.future.Future;

import reactor.core.publisher.Mono;

public interface RBaseService{
	
	/**
	 * 订阅并返回GGXFuture
	 *
	 * @param <T>
	 * @param mono
	 * @return
	 * @author zai
	 * 2020-11-12 14:27:57
	 */
	<T> Future<T, ?> subscribeForFuture(Mono<T> mono);
}
