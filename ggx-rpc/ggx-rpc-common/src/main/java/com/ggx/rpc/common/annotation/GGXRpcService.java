package com.ggx.rpc.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GGXRpcService {
	
	/**
	 * 跨组名称
	 *
	 * @return
	 * 2021-01-17 11:57:31
	 */
	String crossGroup() default "";
	
}
