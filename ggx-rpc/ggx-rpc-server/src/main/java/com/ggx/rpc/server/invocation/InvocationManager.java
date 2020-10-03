package com.ggx.rpc.server.invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFailedFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXSuccessFuture;
import com.ggx.group.common.group.manager.GGSessionGroupManager;
import com.ggx.rpc.common.cache.InterfaceInfo;
import com.ggx.rpc.common.cache.InterfaceInfoParser;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.rpc.server.config.RpcServerConfig;
import com.ggx.util.logger.GGXLoggerUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

/**
 * RPC接口调用管理器
 *
 * @author zai
 * 2020-10-04 01:25:36
 */
public class InvocationManager extends ListenableMapDataManager<String, InvocationInfo>{
	
	private RpcServerConfig config;
	private GGSessionGroupManager sessionGroupManager;
	
	private ParameterSerializerFactory parameterSerializerFactory;
	
	public InvocationManager(RpcServerConfig config) {
		this.config = config;
		this.parameterSerializerFactory = config.getParameterSerializerFactory();
		this.sessionGroupManager = this.config.getSessionGroupServer().getConfig().getSessionGroupManager();
	}

	public void register(Class<?> serviceInterface, Object instance) {
		InterfaceInfoParser interfaceInfoParser = this.config.getInterfaceInfoParser();
		InterfaceInfo interfaceInfo = interfaceInfoParser.parse(serviceInterface);
		
		InvocationInfo invocationInfo = new InvocationInfo();
		invocationInfo.setInstance(instance);
		invocationInfo.setInterfaceInfo(interfaceInfo);
		
		this.put(interfaceInfo.getInterfaceName(), invocationInfo);
	}
	
	
	public GGXFuture invoke(String interfaceName, String methodName, List<byte[]> paramBytes) {
		try {
			InvocationInfo invocationInfo = this.get(interfaceName);
			
			if (invocationInfo == null) {
				GGXLoggerUtil.getLogger(this).error("Can not find interface info '{}'!", interfaceName);
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			
			InterfaceInfo interfaceInfo = invocationInfo.getInterfaceInfo();
			Map<String, Method> methods = interfaceInfo.getMethods();
			Method method = methods.get(methodName);
			if (method == null) {
				GGXLoggerUtil.getLogger(this).error("Can not find the method '{}' in interface class '{}'!", methodName, interfaceName);
				return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
			}
			
			Object obj = invocationInfo.getInstance();
			Object[] args = null;
			
			if (paramBytes != null && paramBytes.size() > 0) {
				args = new Object[paramBytes.size()];
				Map<Method, Class<?>[]> methodParamTypes = interfaceInfo.getMethodParamTypes();
				Class<?>[] paramTypes = methodParamTypes.get(method);
				for (int i = 0; i < paramTypes.length; i++) {
					byte[] bytes = paramBytes.get(i);
					Class<?> pt = paramTypes[i];
					ParameterSerializer<?> serializer = this.parameterSerializerFactory.getSerializer(pt);
					args[i] = serializer.deserialize(bytes, pt);
				}
			}
		
			Object result = method.invoke(obj, args);
			if (result instanceof GGXFuture) {
				return (GGXFuture) result;
			}
			
			return new GGXDefaultFuture(true, result);
			
		} catch (Exception e) {
			GGXLoggerUtil.getLogger(this).error("RPC invoke ERROR !! method '{}' ,interface class '{}'", methodName, interfaceName);
		}
		return GGXFailedFuture.DEFAULT_FAILED_FUTURE;
	}
	
}
