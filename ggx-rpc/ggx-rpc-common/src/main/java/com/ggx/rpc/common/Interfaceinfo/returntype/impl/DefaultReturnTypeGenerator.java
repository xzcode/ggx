package com.ggx.rpc.common.Interfaceinfo.returntype.impl;

import java.lang.reflect.Method;

import com.ggx.rpc.common.Interfaceinfo.returntype.ReturnTypeGenerator;

public class DefaultReturnTypeGenerator implements ReturnTypeGenerator{
	
	private Class<?> returnType;

	public DefaultReturnTypeGenerator(Class<?> returnType) {
		super();
		this.returnType = returnType;
	}

	@Override
	public Class<?> generateReturnType(Method method, Object[] params) {
		return this.returnType;
	}

}
