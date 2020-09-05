package com.ggx.server.spring.boot.starter.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GGXMessageFilter {
	
	/**
	 * 序号
	 * @return
	 * 
	 * @author zai
	 * 2017-09-27
	 */
	 int value() default 0;
	 
	
}
