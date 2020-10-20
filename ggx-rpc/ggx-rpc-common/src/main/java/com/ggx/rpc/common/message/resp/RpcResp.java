package com.ggx.rpc.common.message.resp;

import com.ggx.core.common.message.model.AbstractMessage;

public class RpcResp extends AbstractMessage {

	private String rpcId;
	
	private byte[] returnData;
	
	private boolean success = true;

	public RpcResp() {
		super();
	}
	
	public RpcResp(String rpcId, boolean success) {
		this.rpcId = rpcId;
		this.success = success;
	}

	public RpcResp(String rpcId, byte[] returnData, String returnDataType) {
		super();
		this.rpcId = rpcId;
		this.returnData = returnData;
	}
	
	public RpcResp(String rpcId, byte[] returnData) {
		super();
		this.rpcId = rpcId;
		this.returnData = returnData;
	}

	public String getRpcId() {
		return rpcId;
	}

	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}

	public byte[] getReturnData() {
		return returnData;
	}

	public void setReturnData(byte[] returnData) {
		this.returnData = returnData;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}

}
