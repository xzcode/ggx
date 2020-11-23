package com.ggx.spring.common.mongo.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.spring.common.base.service.remote.RBaseService;

import reactor.core.publisher.Mono;

public abstract class RAbstractBaseService  implements RBaseService{
	
	@Autowired
	protected MongoTemplate mongoTemplate;

	@Autowired
	protected ReactiveMongoTemplate reactiveMongoTemplate;

	public <T> GGXFuture<T> subscribeForFuture(Mono<T> mono) {
		GGXDefaultFuture<T> future = new GGXDefaultFuture<>();
		mono.switchIfEmpty(Mono.create(o -> {
			future.setSuccess(true);
			future.setDone(true);
		})).subscribe(s -> {
			future.setSuccess(true);
			future.setData(s);
			future.setDone(true);
		}, err -> {
			future.setSuccess(false);
			future.setDone(true);
			future.setCause(err);
		});
		return future;
	}

}
