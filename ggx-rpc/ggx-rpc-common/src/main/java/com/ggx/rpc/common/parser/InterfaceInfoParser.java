package com.ggx.rpc.common.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ggx.rpc.common.annotation.GGXRpcService;
import com.ggx.rpc.common.annotation.GGXRpcTargetService;
import com.ggx.util.reflect.GGXReflectUtil;

/***
 * 代理接口解析器
 *
 * @author zai 2020-10-03 23:56:22
 */
public class InterfaceInfoParser {

	public InterfaceInfo parse(Class<?> proxyInterface) {

		InterfaceInfo interfaceInfo = new InterfaceInfo();

		interfaceInfo.setInterfaceClass(proxyInterface);

		interfaceInfo.setInterfaceName(proxyInterface.getCanonicalName());

		GGXRpcService annotatedRpcInterface = proxyInterface.getAnnotation(GGXRpcService.class);
		if (annotatedRpcInterface != null) {
			Class<?> fallback = annotatedRpcInterface.fallback();
			if (fallback != null) {
				interfaceInfo.setFallbackClass(fallback);
				interfaceInfo.setFallbackClassName(fallback.getCanonicalName());
			}
		}

		Map<String, Method> methods = new HashMap<>();
		// 方法
		Map<Method, Class<?>[]> methodParamTypes = new HashMap<>();

		// 方法注解类型集合
		// Map<Method, Class<?>[]> methodAnnotatedTypes = new HashMap<>();

		// 返回类型
		Map<Method, Class<?>> methodReturnClasses = new HashMap<>();
		
		// 方法指定服务id参数下标集合
		Map<Method, Integer> methodTargetServiceParamIndexes = new HashMap<>();

		// 返回类型泛型集合
		Map<Method, List<Class<?>>> methodGenericReturnTypes = new HashMap<>();

		List<Method> declaredMethods = GGXReflectUtil.getAllDeclaredMethods(proxyInterface);

		for (Method mtd : declaredMethods) {

			Class<?>[] parameterTypes = mtd.getParameterTypes();
			methodParamTypes.put(mtd, parameterTypes);

			methods.put(makeMethodName(mtd, parameterTypes), mtd);
			Annotation[][] parameterAnnotations = mtd.getParameterAnnotations();
			for (int i = 0; i < parameterTypes.length; i++) {
				
				boolean findGGXRpcTargetService =false;
				Annotation[] annos = parameterAnnotations[i];
				if (annos != null && annos.length > 0) {
					for (int j = 0; j < annos.length; j++) {
						//添加目标服务注解信息缓存
						Annotation curAnn = annos[j];
						if (curAnn.annotationType() == GGXRpcTargetService.class) {
							methodTargetServiceParamIndexes.put(mtd, i);
							findGGXRpcTargetService = true;
							break;
						}
					}
					
				}
				
				if (findGGXRpcTargetService) {
					break;
				}
				

			}

			Class<?> returnType = mtd.getReturnType();
			if (returnType == List.class) {
				methodReturnClasses.put(mtd, ArrayList.class);
			} else if (returnType == Map.class) {
				methodReturnClasses.put(mtd, LinkedHashMap.class);
			} else {
				methodReturnClasses.put(mtd, returnType);
			}

			// 获取返回值的泛型参数
			Type genericReturnType = mtd.getGenericReturnType();

			if (genericReturnType instanceof ParameterizedType) {
				List<Class<?>> genericReturnTypeList = new ArrayList<>();
				Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
				for (Type type : actualTypeArguments) {
					if (type.getTypeName() == "?") {
						genericReturnTypeList.add(Object.class);
					} else {
						genericReturnTypeList.add((Class<?>) type);
					}
				}
				methodGenericReturnTypes.put(mtd, genericReturnTypeList);
			}
		}

		interfaceInfo.setMethods(methods);
		interfaceInfo.setMethodParamTypes(methodParamTypes);
		interfaceInfo.setMethodReturnClasses(methodReturnClasses);
		interfaceInfo.setMethodGenericReturnTypes(methodGenericReturnTypes);
		interfaceInfo.setMethodTargetServiceParamIndexes(methodTargetServiceParamIndexes);
		
		return interfaceInfo;
	}

	public String makeMethodName(Method method, Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder(32);
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
