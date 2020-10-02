package com.ggx.rpc.common.message.req;

import java.util.List;

import com.ggx.core.common.message.model.AbstractMessage;
import com.ggx.rpc.common.message.model.BytesModel;

/**
 * 事件订发布请求
 *
 * @author zai 2020-04-06 18:50:10
 */
public class RpcReq extends AbstractMessage{

	//接口名称
	private String interfaceName;

	// 方法名称
	private String methodName;
	
	//参数名列表
	private List<String> paramNames;
	
	//参数名列表
	private List<BytesModel> paramDatas;

	public RpcReq() {

	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String className) {
		this.interfaceName = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getParamNames() {
		return paramNames;
	}

	public void setParamNames(List<String> paramNames) {
		this.paramNames = paramNames;
	}

	public List<BytesModel> getParamDatas() {
		return paramDatas;
	}

	public void setParamDatas(List<BytesModel> paramDatas) {
		this.paramDatas = paramDatas;
	}
	
	

}
