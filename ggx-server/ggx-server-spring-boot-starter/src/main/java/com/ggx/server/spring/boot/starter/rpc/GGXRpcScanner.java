package com.ggx.server.spring.boot.starter.rpc;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.ggx.rpc.common.annotation.GGXRpcService;

public class GGXRpcScanner extends ClassPathBeanDefinitionScanner {

	public GGXRpcScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(registry, useDefaultFilters);
	}
	
	@Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        addIncludeFilter(new AnnotationTypeFilter(GGXRpcService.class));
        return super.doScan(basePackages);
    }

}
