package com.ggx.rpc.common.message.req;

import com.ggx.core.common.message.model.AbstractMessage;

/**
 * 事件订发布请求
 *
 * @author zai 2020-04-06 18:50:10
 */
public class RpcReq extends AbstractMessage {

	// 调用id
	private String rpcId;
	
	// 负载均衡id
	private String loadbalanceId;

	// 接口名称
	private String interfaceName;

	// 方法名称
	private String methodName;

	// 参数数据
	private byte[] paramBytes;
	
	//超时时长 毫秒
	private long timeout;

	public RpcReq() {

	}

	public String getRpcId() {
		return rpcId;
	}

	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
	public long getTimeout() {
		return timeout;
	}
	
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public byte[] getParamBytes() {
		return paramBytes;
	}

	public void setParamBytes(byte[] paramBytes) {
		this.paramBytes = paramBytes;
	}
	
	
	public String getLoadbalanceId() {
		return loadbalanceId;
	}
	
	public void setLoadbalanceId(String loadbalanceId) {
		this.loadbalanceId = loadbalanceId;
	}

}
