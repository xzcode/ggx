package com.ggx.spring.common.mongo.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class CustomMongodbConfig {

	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
		mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
		
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		
		context.setFieldNamingStrategy(new CustomFieldNamingStrategy());
		
		return mappingConverter;
	}
	
	@Bean
	public MongoIndexesConfigBean mongoIndexesConfigBean() {
		return new MongoIndexesConfigBean();
	}
	
}
