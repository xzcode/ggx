package com.ggx.core.spring.support.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GGXEventHandler {
	
	String value();

}
