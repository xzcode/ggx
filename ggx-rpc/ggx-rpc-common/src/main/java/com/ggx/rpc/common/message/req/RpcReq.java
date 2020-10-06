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
	
	

	/*
	 * public static void main(String[] args) throws Exception {
	 * ProtoStuffSerializer serializer = new ProtoStuffSerializer();
	 * 
	 * RpcReq rpcReq = new RpcReq(); rpcReq.setInterfaceName("interface01");
	 * rpcReq.setMethodName("method01"); rpcReq.setParamDatas(Arrays.asList(new
	 * byte[] { 1, 2, 3 }, new byte[] { 3, 2, 1 })); byte[] serialize =
	 * serializer.serialize(rpcReq); System.out.println(Arrays.toString(serialize));
	 * RpcReq deserialize = serializer.deserialize(serialize, RpcReq.class);
	 * System.out.println(deserialize);
	 * 
	 * 
	 * TestA<TestB> testA = new TestA<TestB>(); testA.setId("12123");
	 * testA.setDataT(new TestB("xxx", 123));
	 * 
	 * byte[] bytes = serializer.serialize(testA);
	 * System.out.println(Arrays.toString(bytes)); System.out.println(bytes.length);
	 * TestA testA2 = serializer.deserialize(bytes, TestA.class);
	 * System.out.println(testA2);
	 * 
	 * }
	 */

}
