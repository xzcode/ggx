package com.ggx.server.spring.boot.starter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.ggx.server.spring.boot.starter.rpc.GGXRpcScannerRegistrar;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GGXRpcScannerRegistrar.class)
public @interface GGXRpcScan {

	String[] basePackage() default {};

}
