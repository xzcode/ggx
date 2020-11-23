package com.ggx.spring.common.ggx.message;

import com.ggx.docs.core.annotation.DocsModelProperty;

/**
 * 基础响应消息
 *
 * @author zai
 * 2020-09-29 14:52:16
 */
public class BaseResp extends BaseMessage{
	
	@DocsModelProperty("是否成功")
	protected boolean respSuccess = true;
	
	@DocsModelProperty("响应码")
	protected String respCode;
	
	@DocsModelProperty("响应消息内容")
	protected String respMsg;
	
	
	
	public BaseResp() {
	}
	public BaseResp(boolean respSuccess) {
		this.respSuccess = respSuccess;
	}
	
	public BaseResp(String respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public boolean isRespSuccess() {
		return respSuccess;
	}
	public void setRespSuccess(boolean respSuccess) {
		this.respSuccess = respSuccess;
	}
	
	
	
}
