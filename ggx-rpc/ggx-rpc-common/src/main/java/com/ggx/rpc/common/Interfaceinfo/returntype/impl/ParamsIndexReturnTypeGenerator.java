package com.ggx.rpc.common.Interfaceinfo.returntype.impl;

import java.lang.reflect.Method;

import com.ggx.rpc.common.Interfaceinfo.returntype.ReturnTypeGenerator;

public class ParamsIndexReturnTypeGenerator implements ReturnTypeGenerator{
	
	private Integer returnTypeParamsIndex;

	public ParamsIndexReturnTypeGenerator(Integer returnTypeParamsIndex) {
		this.returnTypeParamsIndex = returnTypeParamsIndex;
	}

	@Override
	public Class<?> generateReturnType(Method method, Object[] params) {
		return (Class<?>) params[returnTypeParamsIndex];
	}

}
