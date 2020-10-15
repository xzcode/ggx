package com.ggx.core.common.session.id;

import com.ggx.util.id.GGXRandomIdUtil;

public class DefaultSessionIdGenerator implements SessionIdGenerator {

	@Override
	public String generateSessionId() {
		return GGXRandomIdUtil.newRandomStringId24();
	}

}
