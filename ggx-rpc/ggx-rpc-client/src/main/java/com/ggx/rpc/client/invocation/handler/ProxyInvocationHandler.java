package com.ggx.rpc.client.invocation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.ggx.core.common.future.GGXDefaultFuture;
import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.factory.GGXFutureFactory;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.exception.RpcServiceNoFallbackException;
import com.ggx.rpc.client.exception.RpcServiceNotReadyException;
import com.ggx.rpc.client.exception.RpcServiceSendMessageFailedException;
import com.ggx.rpc.client.proxy.RpcProxyInfo;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.client.service.callback.RpcMethodCallback;
import com.ggx.rpc.client.service.callback.RpcMethodCallbackManager;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.common.parser.InterfaceInfo;
import com.ggx.rpc.common.parser.InterfaceInfoParser;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.util.id.GGXRandomIdUtil;

public class ProxyInvocationHandler implements InvocationHandler {
	
	private RpcClientConfig config;
	private Class<?> serviceInterface;
	private Object fallbackObj;
	private InterfaceServiceGroupCache interfaceServiceGroupCache;
	private RpcProxyManager proxyManager;
	private InterfaceInfoParser interfaceInfoParser;
	private ParameterSerializerFactory serializerFactory;
	private RpcMethodCallbackManager rpcMethodCallbackManager;
	

	public ProxyInvocationHandler(RpcClientConfig config, Class<?> serviceInterface, Object fallbackObj) {
		this.config = config;
		this.serviceInterface = serviceInterface;
		this.fallbackObj = fallbackObj;
		this.interfaceServiceGroupCache = this.config.getInterfaceServiceGroupCache();
		this.proxyManager = this.config.getProxyManager();
		this.interfaceInfoParser = this.config.getInterfaceInfoParser();
		this.serializerFactory = this.config.getParameterSerializerFactory();
		this.rpcMethodCallbackManager = this.config.getRpcMethodCallbackManager();
	}

	@Override
	public Object invoke(Object proxy, Method proxyMethod, Object[] args) throws Throwable {
		
		//确认需要调用的服务
		RpcProxyInfo proxyInfo = proxyManager.get(serviceInterface);
		InterfaceInfo interfaceInfo = proxyInfo.getInterfaceInfo();
		Map<String, Method> methods = interfaceInfo.getMethods();
		

		String serviceName = serviceInterface.getCanonicalName();
		String methodName = interfaceInfoParser.makeMethodName(proxyMethod, proxyMethod.getParameterTypes());
		Method method = methods.get(interfaceInfoParser.makeMethodName(proxyMethod, proxyMethod.getParameterTypes()));
		
		if (method == null) {
			if (this.fallbackObj == null) {
				throw new RpcServiceNoFallbackException(serviceName, methodName);
			}
			return proxyMethod.invoke(this.fallbackObj, args);				
		}
		
		Class<?>[] paramTypes = proxyInfo.getInterfaceInfo().getMethodParamTypes().get(method);
		
		
		RpcServiceGroup group = interfaceServiceGroupCache.get(serviceInterface);
		
		Map<Method, Class<?>> methodReturnClasses = interfaceInfo.getMethodReturnClasses();
		Class<?> returnType = methodReturnClasses.get(method);
		
		//判断是否异步回调
		boolean async = returnType == GGXFuture.class;
		
		
		if (group == null) {
			RpcServiceNotReadyException notReadyException = new RpcServiceNotReadyException(serviceName);
			if (async) {
				return GGXFutureFactory.fail(notReadyException);
			}
			if (this.fallbackObj == null) {
				throw new RpcServiceNoFallbackException(serviceName, methodName);
			}
			return proxyMethod.invoke(this.fallbackObj, args);
		}
		
		//组装数据包
		RpcReq req = new RpcReq();
		req.setRpcId(GGXRandomIdUtil.newRandomStringId24());
		req.setInterfaceName(interfaceInfo.getInterfaceName());
		req.setMethodName(methodName);
		if (paramTypes != null && paramTypes.length > 0) {
			ParameterSerializer<?> serializer = serializerFactory.getDefaultSerializer();
			byte[] paramBytes = serializer.serialize(args);
			req.setParamBytes(paramBytes);
		}
		
		//回调处理
		RpcMethodCallback callback = new RpcMethodCallback();
		callback.setRpcId(req.getRpcId());
		callback.setTimeout(this.config.getRpcTimeout());
		callback.setReturnType(returnType);
		callback.setAsync(async);
		callback.setCallbackFuture(new GGXDefaultFuture());
		callback.setServiceName(serviceName);
		
		//发送数据包
		GGXFuture invokeFuture = group.invoke(req);
		invokeFuture.addListener(f -> {
			if (!f.isSuccess()) {
				GGXDefaultFuture callbackFuture = callback.getCallbackFuture();
				callbackFuture.setSuccess(false);
				callbackFuture.setDone(true);
				callbackFuture.setCause(new RpcServiceSendMessageFailedException(callback.getServiceName()));
				if (!callback.isAsync()) {
					synchronized(callback) {
						if (callback.isWaiting()) {
							callback.notify();
						}
						callback.setNotified(true);
					}
				}
			}
		});
		
		
		
		this.rpcMethodCallbackManager.put(callback.getRpcId(), callback);
		
		//如果是异步，直接返回future
		if (callback.isAsync()) {
			return callback.getCallbackFuture();
		}
		
		synchronized(callback) {
			if (!callback.isNotified()) {
				callback.setWaiting(true);
				//如果是同步，挂起线程等待
				callback.wait();
			}
		}
		return callback.getCallbackFuture().get();
		
	}

}
