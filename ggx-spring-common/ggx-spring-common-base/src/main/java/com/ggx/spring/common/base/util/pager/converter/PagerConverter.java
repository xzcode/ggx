package com.ggx.spring.common.base.util.pager.converter;

import com.ggx.spring.common.base.util.pager.Pager;

public interface PagerConverter<E, T> {
	
	void convert(Pager<E> src, Pager<T> target);

}
