package com.ggx.core.common.message.actionid.impl;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.actionid.ActionIdGenerator;
import com.ggx.core.common.utils.ClassNameGenerateIdUtil;

public class DefaultActionIdGenerator implements ActionIdGenerator{
	
	private GGXCoreConfig config;
	
	public DefaultActionIdGenerator(GGXCoreConfig config) {
		super();
		this.config = config;
	}

	@Override
	public String generate(Class<?> clazz) {
		String actionId = ClassNameGenerateIdUtil.generateClassNameDotId(clazz);
		boolean startWithGGX = (actionId.startsWith(config.getGgxComponentAtionIdPrefix()) || actionId.startsWith(config.getGgxComponentAtionIdPrefix().toLowerCase()));
		if (!startWithGGX) {
			if (config.getActionIdPrefix() != null && !config.getActionIdPrefix().isEmpty()) {
				actionId = config.getActionIdPrefix() + actionId;
			}
			if (clazz.getPackage().getName().startsWith(config.getGGXBasePackage())) {
				actionId = (config.getGgxComponentAtionIdPrefix() + actionId).toUpperCase();
			}
		}
		return actionId;
	}

}
