package com.ggx.rpc.common.Interfaceinfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.ggx.rpc.common.Interfaceinfo.returntype.ReturnTypeGenerator;

/***
 * 代理接口信息类
 *
 * @author zai 2020-10-03 23:56:22
 */
public class InterfaceInfo {

	// 接口名称
	protected String interfaceName;
	
	// 跨组名称
	protected String crossGroup;

	// 接口class
	protected Class<?> interfaceClass;

	// 备用实例class name
	protected String fallbackClassName;

	// 备用实例class
	protected Class<?> fallbackClass;

	// 所有方法集合
	protected Map<String, Method> methods;

	// 方法参数类型
	protected Map<Method, Class<?>[]> methodParamTypes;
	
	// 方法指定服务id参数下标集合
	protected Map<Method, Integer> methodTargetServiceParamIndexes;
	
	// 方法指定服务组id参数下标集合
	protected Map<Method, Integer> methodTargetGroupParamIndexes;

	// 方法异步集合
	protected Map<Method, Boolean> methodAsyncMap;

	// 方法注解类型集合
	protected Map<Method, Class<?>[]> methodAnnotatedTypes;

	// 返回类型
	protected Map<Method, ReturnTypeGenerator> methodReturnClasses;

	// 返回类型泛型集合
	protected Map<Method, List<ReturnTypeGenerator>> methodGenericReturnTypes;

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

	public Map<Method, Integer> getMethodTargetServiceParamIndexes() {
		return methodTargetServiceParamIndexes;
	}
	public void setMethodTargetServiceParamIndexes(Map<Method, Integer> methodTargetServiceParamIndexes) {
		this.methodTargetServiceParamIndexes = methodTargetServiceParamIndexes;
	}

	public Map<Method, ReturnTypeGenerator> getMethodReturnClasses() {
		return methodReturnClasses;
	}

	public void setMethodReturnClasses(Map<Method, ReturnTypeGenerator> methodReturnClasses) {
		this.methodReturnClasses = methodReturnClasses;
	}

	public Map<Method, List<ReturnTypeGenerator>> getMethodGenericReturnTypes() {
		return methodGenericReturnTypes;
	}

	public void setMethodGenericReturnTypes(Map<Method, List<ReturnTypeGenerator>> methodGenericReturnTypes) {
		this.methodGenericReturnTypes = methodGenericReturnTypes;
	}
	
	public Map<Method, Integer> getMethodTargetGroupParamIndexes() {
		return methodTargetGroupParamIndexes;
	}
	
	public void setMethodTargetGroupParamIndexes(Map<Method, Integer> methodTargetGroupParamIndexes) {
		this.methodTargetGroupParamIndexes = methodTargetGroupParamIndexes;
	}
	
	public String getCrossGroup() {
		return crossGroup;
	}
	
	public void setCrossGroup(String crossGroup) {
		this.crossGroup = crossGroup;
	}
	
	
}
