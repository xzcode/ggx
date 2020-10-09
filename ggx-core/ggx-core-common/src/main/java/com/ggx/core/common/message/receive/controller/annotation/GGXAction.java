package com.ggx.core.common.message.receive.controller.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GGXAction {
	
	Class<?> value() default Void.class;
	
	Class<?> onMessage() default Void.class;
}
