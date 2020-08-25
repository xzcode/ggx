package com.ggx.group.server.session;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.session.factory.DefaultChannelSessionFactory;

public class ServiceServerSessionFactory extends DefaultChannelSessionFactory {

	public ServiceServerSessionFactory(GGXCoreConfig config) {
		super(config);
	}

}
