package com.ggx.spring.common.mongo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.util.ParsingUtils;

/**
 * 自定义字段命名策略
 *
 * @author zai
 * 2020-11-12 19:07:26
 */
public class CustomFieldNamingStrategy implements FieldNamingStrategy {

	@Override
	public String getFieldName(PersistentProperty<?> property) {
		return StringUtils.join(ParsingUtils.splitCamelCaseToLower(property.getName()), "_");
	}

}
