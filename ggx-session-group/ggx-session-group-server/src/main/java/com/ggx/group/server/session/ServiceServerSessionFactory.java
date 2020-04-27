package com.ggx.group.server.session;

import com.ggx.core.common.config.GGConfig;
import com.ggx.core.common.session.factory.DefaultChannelSessionFactory;

public class ServiceServerSessionFactory extends DefaultChannelSessionFactory {

	public ServiceServerSessionFactory(GGConfig config) {
		super(config);
	}

}
