package com.ggx.router.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 路由会话过期请求
 *
 * @author zai
 * 2020-05-11 11:31:30
 */
public class RouterSessionExpireReq implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + "ROUTE.SESSION.EXP.REQ";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 路由的会话id
	private String routeSessionId;

	public RouterSessionExpireReq() {

	}

	public String getRouteSessionId() {
		return routeSessionId;
	}

	public void setRouteSessionId(String tranferSessionId) {
		this.routeSessionId = tranferSessionId;
	}

}
