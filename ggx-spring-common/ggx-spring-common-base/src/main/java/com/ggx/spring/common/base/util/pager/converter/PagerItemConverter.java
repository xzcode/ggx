package com.ggx.spring.common.base.util.pager.converter;

public interface PagerItemConverter<E, T> {
	
	void convert(E src, T target);

}
