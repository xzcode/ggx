package com.ggx.files;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ggx.files.service.entity._GGXFilesEntityPackageClass;
import com.ggx.spring.common.mongo.config.GGXSpringCommonMongodbConfig;

@Configuration
@Import(GGXSpringCommonMongodbConfig.class)
@EntityScan(basePackageClasses = {_GGXFilesEntityPackageClass.class})
@ComponentScan
public class GGXFilesServiceAutoConfig {
	
}
