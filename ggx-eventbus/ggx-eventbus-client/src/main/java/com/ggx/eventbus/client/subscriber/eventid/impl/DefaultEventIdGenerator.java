package com.ggx.eventbus.client.subscriber.eventid.impl;

import com.ggx.core.common.utils.ClassNameGenerateIdUtil;
import com.ggx.eventbus.client.subscriber.eventid.EventIdGenerator;

public class DefaultEventIdGenerator implements EventIdGenerator{
	

	@Override
	public String generate(Class<?> clazz) {
		return ClassNameGenerateIdUtil.generateClassNameSublineSplitId(clazz).toUpperCase();
	}

}
