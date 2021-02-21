package com.ggx.router.common.message.req;

import java.util.ArrayList;
import java.util.List;

import com.ggx.core.common.message.model.Message;
import com.ggx.router.common.message.model.TranferSessionAttrModel;

/**
 * 创建会话请求
 *
 * 2021-02-21 19:36:46
 */
public class RouterCreateSessionReq implements Message{
	
	// 传递的会话id
	private String tranferSessionId;
	
	// 传递的属性
	private List<TranferSessionAttrModel> tranferSessionAttrs = new ArrayList<TranferSessionAttrModel>();
	
	/**
	 * 添加属性传递
	 *
	 * @param attr
	 * 2021-02-21 19:36:35
	 */
	public void addAttr(TranferSessionAttrModel attr) {
		this.tranferSessionAttrs.add(attr);
	}

	public String getTranferSessionId() {
		return tranferSessionId;
	}

	public void setTranferSessionId(String tranferSessionId) {
		this.tranferSessionId = tranferSessionId;
	}

	public List<TranferSessionAttrModel> getTranferSessionAttrs() {
		return tranferSessionAttrs;
	}
	
	public void setTranferSessionAttrs(List<TranferSessionAttrModel> tranferSessionAttrs) {
		this.tranferSessionAttrs = tranferSessionAttrs;
	}

}
