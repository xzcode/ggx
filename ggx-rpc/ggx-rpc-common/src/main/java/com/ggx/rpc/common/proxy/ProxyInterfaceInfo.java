package com.ggx.rpc.common.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.ggx.core.common.handler.serializer.Serializer;
import com.ggx.core.common.handler.serializer.impl.JsonSerializer;

public class ProxyInterfaceInfo {
	
	protected Class<?> interfaceClass;
	
	protected Map<String, Method> methods;
	
	protected Map<Method, Class<?>[]> methodParamTypes;
	
	//方法注解类型集合
	protected Map<Method, Class<?>[]> methodAnnotatedTypes;
	
	//返回类型
	protected Class<?> methodReturnClass;
	
	//返回类型泛型集合
	protected List<Class<?>> methodGenericReturnTypes;
	
	public Class<ProxyInterfaceInfo> name() {
		return null;
	}
	
	public static void main(String[] args) {
		
		ProxyInterfaceInfo proxyInterfaceInfo = new ProxyInterfaceInfo();
		
		Type genericReturnType = proxyInterfaceInfo.getClass().getMethods()[1].getGenericReturnType();
		
		//获取返回值的泛型参数
        if (genericReturnType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            for (Type type : actualTypeArguments) {
                System.out.println("type " + (Class<?>)type);
            }
        }
		
	}

}
