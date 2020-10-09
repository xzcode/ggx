package com.ggx.core.common.message.actionid.impl;

import java.util.List;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.message.actionid.AddActionIdPrefixHandler;

public class DefaultAddActionIdPrefixHandler implements AddActionIdPrefixHandler{
	
	protected GGXCoreConfig coreConfig;
	
	

	public DefaultAddActionIdPrefixHandler(GGXCoreConfig coreConfig) {
		super();
		this.coreConfig = coreConfig;
	}


	@Override
	public String handle(String oldAction) {
		String actionId = oldAction;
		String actionIdPrefix = coreConfig.getActionIdPrefix();
		boolean ggxComponent = coreConfig.isGgxComponent();
		String ggxComponentAtionIdPrefix = coreConfig.getGgxComponentAtionIdPrefix();
		List<String> ignoreActionIdPrefixes = coreConfig.getIgnoreActionIdPrefixes();
		boolean startWithGGX = (actionId.startsWith(coreConfig.getGgxComponentAtionIdPrefix()) || actionId.startsWith(coreConfig.getGgxComponentAtionIdPrefix().toLowerCase()));
		if (!startWithGGX) {
			boolean ignore = false;
			for (String ignoreActionIdPrefix : ignoreActionIdPrefixes) {
				ignore = actionId.startsWith(ignoreActionIdPrefix);
				if (ignore) {
					break;
				}
			}
			if (!ignore &&  actionIdPrefix != null && !actionId.startsWith(actionIdPrefix)) {
				actionId = actionIdPrefix  + actionId;
			}
			if (ggxComponent) {
				actionId = (ggxComponentAtionIdPrefix + actionId).toUpperCase();
			}
		}
		return actionId;
	}

}
