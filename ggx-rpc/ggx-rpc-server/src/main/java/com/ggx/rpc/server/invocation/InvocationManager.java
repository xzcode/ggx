package com.ggx.rpc.server.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.factory.GGXFutureFactory;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.common.model.InterfaceInfoModel;
import com.ggx.rpc.common.parser.InterfaceInfo;
import com.ggx.rpc.common.parser.InterfaceInfoParser;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.util.logger.GGXLogUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * RPC接口调用管理器
 *
 * @author zai
 * 2020-10-04 01:25:36
 */
public class InvocationManager extends ListenableMapDataManager<String, InvocationInfo>{
	
	private RpcServerConfig config;
	
	private ParameterSerializerFactory parameterSerializerFactory;
	
	public InvocationManager(RpcServerConfig config) {
		this.config = config;
		this.parameterSerializerFactory = config.getParameterSerializerFactory();
	}

	public void register(Class<?> serviceInterface, Object instance) {
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		InvocationInfo invocationInfo = new InvocationInfo();
		invocationInfo.setInstance(instance);
		invocationInfo.setInterfaceInfo(interfaceInfo);
		
		this.put(interfaceInfo.getInterfaceName(), invocationInfo);
		
		GGXLogUtil.getLogger(this).debug("Registered RPC Invocation Service! Interface [{}] , Instance[{}]!", serviceInterface.getCanonicalName(), instance.getClass().getCanonicalName());
	}
	
	
	public GGXFuture invoke(RpcReq req) {
		String interfaceName = req.getInterfaceName();
		String methodName =req.getMethodName();
		byte[] paramBytes = req.getParamBytes();
		try {
			InvocationInfo invocationInfo = this.get(interfaceName);
			
			if (invocationInfo == null) {
				GGXLogUtil.getLogger(this).error("Can not find interface info '{}'!", interfaceName);
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			
			InterfaceInfo interfaceInfo = invocationInfo.getInterfaceInfo();
			Map<String, Method> methods = interfaceInfo.getMethods();
			Method method = methods.get(methodName);
			if (method == null) {
				GGXLogUtil.getLogger(this).error("Can not find the method '{}' in interface class '{}'!", methodName, interfaceName);
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			
			Object obj = invocationInfo.getInstance();
			Object[] args = null;
			
			if (paramBytes != null && paramBytes.length> 0) {
				 ParameterSerializer<?> serializer = this.parameterSerializerFactory.getSerializer(String.class);
				 args = (Object[]) serializer.deserialize(paramBytes, Object[].class);
			}
		
			Object result = null;
			if (args != null) {
				result = method.invoke(obj, args);
			}else {
				result = method.invoke(obj);
			}
			if (result instanceof GGXFuture) {
				return (GGXFuture) result;
			}
			
			return new GGXDefaultFuture(true, result);
			
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException)e).getTargetException();
			}
			GGXLogUtil.getLogger(this).error("RPC invoke ERROR !! method '{}' ,interface class '{}'", methodName, interfaceName,e);
			return GGXFutureFactory.fail(e);
		}
	}
	
	/**
	 * 获取接口名称列表字符串
	 * @return
	 * @author zai
	 * 2020-10-4 10:28:46
	 */
	public String getInterfaceNameListString() {
		StringBuffer sb = new StringBuffer();
		for (String key : this.getMap().keySet()) {
			sb.append(key).append(",");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 获取接口信息模型集合
	 * @return list
	 * @author zai
	 * 2020-10-4 11:13:10
	 */
	public List<InterfaceInfoModel> getInterfaceInfoModelList() {
		List<InterfaceInfoModel>  list = new ArrayList<InterfaceInfoModel>();
		for (Entry<String, InvocationInfo> entry : this.getMap().entrySet()) {
			InvocationInfo info = entry.getValue();
			InterfaceInfo interfaceInfo = info.getInterfaceInfo();
			list.add(new InterfaceInfoModel(interfaceInfo.getInterfaceName(), interfaceInfo.getFallbackClass().getCanonicalName()));
		}
		return list;
	}
	
}
