package com.ggx.router.common.message.resp;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 路由会话过期推送
 *
 * @author zai
 * 2020-05-11 11:31:30
 */
public class RouterSessionExpireResp implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + "ROUTE.SESSION.EXP.RESP";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 路由的会话id
	private String routeSessionId;

	public RouterSessionExpireResp() {

	}
	
	public RouterSessionExpireResp(String routeSessionId) {
		super();
		this.routeSessionId = routeSessionId;
	}



	public String getRouteSessionId() {
		return routeSessionId;
	}

	public void setRouteSessionId(String tranferSessionId) {
		this.routeSessionId = tranferSessionId;
	}

}
