package com.ggx.server.spring.boot.starter.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GGXController {
	String value() default "";
}
