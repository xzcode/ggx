package com.ggx.rpc.common.Interfaceinfo.returntype;

import java.lang.reflect.Method;

/**
 * 返回类型生成器
 *
 * @author zai
 * 2020-11-19 11:16:28
 */
public interface ReturnTypeGenerator {
	
	/**
	 * 生成返回类型
	 *
	 * @param params
	 * @return
	 * @author zai
	 * 2020-11-19 11:16:37
	 */
	Class<?> generateReturnType(Method method, Object[] params);

}
