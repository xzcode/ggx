package com.ggx.util.bean.converter;

import net.sf.cglib.core.Converter;

public class GGXBeanUtilConverter implements Converter{

	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Object value, Class target, Object context) {
		if (value.getClass() == target) {
			return value;
		}
		return null;
	}

}
