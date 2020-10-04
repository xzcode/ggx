package com.ggx.rpc.common.cache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ggx.rpc.common.annotation.GGXRpcFallback;
import com.ggx.util.reflect.GGXReflectUtil;

/***
 * 代理接口解析器
 *
 * @author zai
 * 2020-10-03 23:56:22
 */
public class InterfaceInfoParser {

	
	public InterfaceInfo parse(Class<?> proxyInterface) {
		
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		
		interfaceInfo.setInterfaceClass(proxyInterface);
		
		interfaceInfo.setInterfaceName(proxyInterface.getCanonicalName());
		
		
		GGXRpcFallback fallback = proxyInterface.getAnnotation(GGXRpcFallback.class);
		if (fallback != null) {
			interfaceInfo.setFallbackClass(fallback.value());
			interfaceInfo.setFallbackClassName(fallback.value().getCanonicalName());
		}
		
		
		Map<String, Method> methods = new HashMap<>();
		// 方法
		Map<Method, Class<?>[]> methodParamTypes = new HashMap<>();

		// 方法注解类型集合
		//Map<Method, Class<?>[]> methodAnnotatedTypes = new HashMap<>();

		// 返回类型
		Map<Method, Class<?>> methodReturnClasses = new HashMap<>();

		// 返回类型泛型集合
		Map<Method, List<Class<?>>> methodGenericReturnTypes = new HashMap<>();
		
		List<Method> declaredMethods = GGXReflectUtil.getAllDeclaredMethods(proxyInterface);
		
		for (Method mtd : declaredMethods) {
			
			Class<?>[] parameterTypes = mtd.getParameterTypes();
			methodParamTypes.put(mtd, parameterTypes);
			
			methods.put(makeMethodKey(mtd, parameterTypes), mtd);
			
			
			/*Class<?> returnType = mtd.getReturnType();
			
			methodReturnClasses.put(mtd, returnType);
			
			// 获取返回值的泛型参数
			Type genericReturnType = mtd.getGenericReturnType();
			
			if (genericReturnType instanceof ParameterizedType) {
				List<Class<?>> genericReturnTypeList = new ArrayList<>();
				Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
				for (Type type : actualTypeArguments) {
					genericReturnTypeList.add((Class<?>) type);
				}
				methodGenericReturnTypes.put(mtd, genericReturnTypeList);
			}*/
		}
		
		interfaceInfo.setMethods(methods);
		interfaceInfo.setMethodParamTypes(methodParamTypes);
		
		return interfaceInfo;
	}
	
	public String makeMethodKey(Method method, Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder(100);
		sb.append(method.getName()).append("(");
		if (parameterTypes != null && parameterTypes.length > 0) {
			for (Class<?> paramType : parameterTypes) {
				sb.append(paramType.getCanonicalName()).append(",");
			}
			sb.setLength(sb.length() - 1);
		}
		sb.append(")");
		return sb.toString();
	}

}
