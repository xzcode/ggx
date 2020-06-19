package com.ggx.core.common.scan.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GGOnEvent {
	
	String value();

}
