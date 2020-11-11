package com.ggx.rpc.common.parser;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/***
 * 代理接口信息类
 *
 * @author zai 2020-10-03 23:56:22
 */
public class InterfaceInfo {

	// 接口名称
	protected String interfaceName;

	// 接口class
	protected Class<?> interfaceClass;

	// 备用实例class name
	protected String fallbackClassName;

	// 目标服务参数下标
	protected Integer targetServiceParamIndex;

	// 目标服务参数类型
	protected Class<?> targetServiceParamType;

	// 备用实例class
	protected Class<?> fallbackClass;

	// 所有方法集合
	protected Map<String, Method> methods;

	// 方法参数类型
	protected Map<Method, Class<?>[]> methodParamTypes;

	// 方法异步集合
	protected Map<Method, Boolean> methodAsyncMap;

	// 方法注解类型集合
	protected Map<Method, Class<?>[]> methodAnnotatedTypes;

	// 返回类型
	protected Map<Method, Class<?>> methodReturnClasses;

	// 返回类型泛型集合
	protected Map<Method, List<Class<?>>> methodGenericReturnTypes;

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Map<String, Method> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Method> methods) {
		this.methods = methods;
	}

	public Map<Method, Class<?>[]> getMethodParamTypes() {
		return methodParamTypes;
	}

	public void setMethodParamTypes(Map<Method, Class<?>[]> methodParamTypes) {
		this.methodParamTypes = methodParamTypes;
	}

	public Map<Method, Class<?>[]> getMethodAnnotatedTypes() {
		return methodAnnotatedTypes;
	}

	public void setMethodAnnotatedTypes(Map<Method, Class<?>[]> methodAnnotatedTypes) {
		this.methodAnnotatedTypes = methodAnnotatedTypes;
	}

	public Map<Method, Class<?>> getMethodReturnClasses() {
		return methodReturnClasses;
	}

	public void setMethodReturnClasses(Map<Method, Class<?>> methodReturnClasses) {
		this.methodReturnClasses = methodReturnClasses;
	}

	public Map<Method, List<Class<?>>> getMethodGenericReturnTypes() {
		return methodGenericReturnTypes;
	}

	public void setMethodGenericReturnTypes(Map<Method, List<Class<?>>> methodGenericReturnTypes) {
		this.methodGenericReturnTypes = methodGenericReturnTypes;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Class<?> getFallbackClass() {
		return fallbackClass;
	}

	public void setFallbackClass(Class<?> fallbackClass) {
		this.fallbackClass = fallbackClass;
	}

	public String getFallbackClassName() {
		return fallbackClassName;
	}

	public void setFallbackClassName(String fallbackClassName) {
		this.fallbackClassName = fallbackClassName;
	}

	public Map<Method, Boolean> getMethodAsyncMap() {
		return methodAsyncMap;
	}

	public void setMethodAsyncMap(Map<Method, Boolean> methodAsyncMap) {
		this.methodAsyncMap = methodAsyncMap;
	}

	public Integer getTargetServiceParamIndex() {
		return targetServiceParamIndex;
	}

	public void setTargetServiceParamIndex(Integer targetServiceParamIndex) {
		this.targetServiceParamIndex = targetServiceParamIndex;
	}

	public Class<?> getTargetServiceParamType() {
		return targetServiceParamType;
	}

	public void setTargetServiceParamType(Class<?> targetServiceParamType) {
		this.targetServiceParamType = targetServiceParamType;
	}
}
