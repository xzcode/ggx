package com.ggx.spring.common.mongo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

public class MongoIndexesConfigBean {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@EventListener(ContextRefreshedEvent.class)
	public void initIndicesAfterStartup() {
		//给所有的实体添加 索引
		MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate.getConverter().getMappingContext();
		IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
		// consider only entities that are annotated with @Document
		mappingContext.getPersistentEntities().stream().filter(it -> it.isAnnotationPresent(Document.class))
				.forEach(it -> {
					IndexOperations indexOps = mongoTemplate.indexOps(it.getType());
					resolver.resolveIndexFor(it.getType()).forEach(indexOps::ensureIndex);
				});
	}
}
