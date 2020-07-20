package com.ggx.admin.server.event;

import com.ggx.admin.collector.server.constant.GGXAdminCollectorServerSessionKeys;
import com.ggx.core.common.event.EventListener;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGSession;

public class CollectorServerConnCloseEventHandler implements EventListener<Void>{

	@Override
	public void onEvent(EventData<Void> eventData) {
		GGSession session = eventData.getSession();
		String serviceId = session.getAttribute(GGXAdminCollectorServerSessionKeys.SERVICE_ID, String.class);
		
		
	}

}
