package com.ggx.router.common.message.req;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.constant.RouterConstant;

/**
 * 路由会话断开连接传递请求
 *
 * @author zai
 * 2020-05-20 11:50:25
 */
public class RouterSessionDisconnectTransferReq implements Message {

	public static final String ACTION_ID = RouterConstant.ACTION_ID_PREFIX + ".SESSION.DISCONNECT.TRANSFER.REQ";

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	// 传递的会话id
	private String tranferSessionId;
	

	public RouterSessionDisconnectTransferReq() {

	}
	
	
	
	public RouterSessionDisconnectTransferReq(String tranferSessionId) {
		super();
		this.tranferSessionId = tranferSessionId;
	}



	public String getTranferSessionId() {
		return tranferSessionId;
	}
	
	public void setTranferSessionId(String tranferSessionId) {
		this.tranferSessionId = tranferSessionId;
	}

	
}
