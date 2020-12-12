package com.ggx.rpc.client.invocation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.ggx.core.common.future.GGXFuture;
import com.ggx.core.common.future.GGXCoreFuture;
import com.ggx.core.common.future.factory.GGXFutureFactory;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.exception.RpcServiceNoFallbackException;
import com.ggx.rpc.client.exception.RpcServiceNotReadyException;
import com.ggx.rpc.client.exception.RpcServiceSendMessageFailedException;
import com.ggx.rpc.client.proxy.RpcProxyInfo;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.callback.RpcMethodCallback;
import com.ggx.rpc.client.service.callback.RpcMethodCallbackManager;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.common.Interfaceinfo.InterfaceInfo;
import com.ggx.rpc.common.Interfaceinfo.InterfaceInfoParser;
import com.ggx.rpc.common.message.req.RpcReq;
import com.ggx.rpc.common.serializer.ParameterSerializer;
import com.ggx.rpc.common.serializer.factory.ParameterSerializerFactory;
import com.ggx.util.id.GGXRandomIdUtil;
import com.ggx.util.logger.GGXLogUtil;

public class ProxyInvocationHandler implements InvocationHandler {

	private RpcClientConfig config;
	private InterfaceServiceGroupCache interfaceServiceGroupCache;
	private RpcProxyManager proxyManager;
	private InterfaceInfoParser interfaceInfoParser;
	private ParameterSerializerFactory serializerFactory;
	private RpcMethodCallbackManager rpcMethodCallbackManager;

	public ProxyInvocationHandler(RpcClientConfig config) {
		this.config = config;
		this.interfaceServiceGroupCache = this.config.getInterfaceServiceGroupCache();
		this.proxyManager = this.config.getProxyManager();
		this.interfaceInfoParser = this.config.getInterfaceInfoParser();
		this.serializerFactory = this.config.getParameterSerializerFactory();
		this.rpcMethodCallbackManager = this.config.getRpcMethodCallbackManager();
	}

	@Override
	public Object invoke(Object proxy, Method proxyMethod, Object[] args) throws Throwable {

		Class<?> serviceInterface = proxyMethod.getDeclaringClass();

		// 确认需要调用的服务
		RpcProxyInfo proxyInfo = proxyManager.get(serviceInterface);

		if (proxyInfo.getTarget() != null) {
			return proxyMethod.invoke(proxyInfo.getTarget(), args);
		}

		InterfaceInfo interfaceInfo = proxyInfo.getInterfaceInfo();
		Map<String, Method> methods = interfaceInfo.getMethods();

		String serviceName = serviceInterface.getCanonicalName();
		String methodName = interfaceInfoParser.makeMethodName(proxyMethod, proxyMethod.getParameterTypes());
		Method method = methods.get(interfaceInfoParser.makeMethodName(proxyMethod, proxyMethod.getParameterTypes()));

		Object fallbackObj = proxyInfo.getFallbackObj();

		if (method == null) {
			if (fallbackObj == null) {
				throw new RpcServiceNoFallbackException(serviceName, methodName);
			}
			return proxyMethod.invoke(fallbackObj, args);
		}

		Class<?>[] paramTypes = proxyInfo.getInterfaceInfo().getMethodParamTypes().get(method);

		RpcServiceGroup group = interfaceServiceGroupCache.get(serviceInterface);
		RpcService rpcService = null;
		String serviceId = null;
		if (group != null) {
			Integer targetServiceParamIndex = interfaceInfo.getMethodTargetServiceParamIndexes().get(method);
			if (targetServiceParamIndex != null) {
				serviceId = String.valueOf(args[targetServiceParamIndex]);
				rpcService = group.get(serviceId);
			} else {
				rpcService = group.getRandomOne();
			}
		}

		Class<?> returnType = interfaceInfo.getMethodReturnClasses().get(method).generateReturnType(method, args);

		// 判断是否异步回调
		boolean async = returnType == GGXFuture.class;

		if (group == null || rpcService == null) {
			RpcServiceNotReadyException notReadyException = new RpcServiceNotReadyException(serviceName);
			if (async) {
				return GGXFutureFactory.fail(notReadyException);
			}

			if (GGXLogUtil.isInfoEnabled()) {
				if (serviceId != null) {
					GGXLogUtil.getLogger(this).info("Target Service [{}] Not Ready!", serviceId);
				} else {
					GGXLogUtil.getLogger(this).info("Service [{}] Not Ready!", serviceName);
				}
			}

			if (fallbackObj == null) {
				throw new RpcServiceNoFallbackException(serviceName, methodName);
			}
			return proxyMethod.invoke(fallbackObj, args);
		}

		// 组装数据包
		RpcReq req = new RpcReq();
		req.setRpcId(GGXRandomIdUtil.newRandomStringId24());
		req.setInterfaceName(interfaceInfo.getInterfaceName());
		req.setMethodName(methodName);
		if (paramTypes != null && paramTypes.length > 0) {
			ParameterSerializer<?> serializer = serializerFactory.getDefaultSerializer();
			byte[] paramBytes = serializer.serialize(args);
			req.setParamBytes(paramBytes);
		}

		// 回调处理
		RpcMethodCallback callback = new RpcMethodCallback();
		callback.setRpcId(req.getRpcId());
		callback.setTimeout(this.config.getRpcTimeout());
		callback.setReturnType(returnType);
		callback.setAsync(async);
		callback.setCallbackFuture(GGXFutureFactory.create());
		callback.setServiceName(serviceName);
		if (async) {
			callback.setAsyncDataType(interfaceInfo.getMethodGenericReturnTypes().get(method).get(0).generateReturnType(method, args));
		}

		callback.getCallbackFuture().addListener(f -> {
			this.rpcMethodCallbackManager.remove(callback.getRpcId());
		});

		this.rpcMethodCallbackManager.put(callback.getRpcId(), callback);

		// 发送数据包
		GGXFuture<?> invokeFuture = rpcService.invoke(req);
		invokeFuture.addListener(f -> {
			if (!f.isSuccess()) {
				GGXFuture<?> timeoutFuture = callback.getTimeoutFuture();
				if (timeoutFuture.cancel()) {
					GGXCoreFuture<?> callbackFuture = callback.getCallbackFuture();
					callbackFuture.setSuccess(false);
					callbackFuture.setDone(true);
					callbackFuture.setCause(new RpcServiceSendMessageFailedException(callback.getServiceName()));
					if (!callback.isAsync()) {
						synchronized (callback) {
							if (callback.isWaiting()) {
								callback.notify();
							}
							callback.setNotified(true);
						}
					}
				}
				return;
			}

		});

		// 如果是异步，直接返回future
		if (callback.isAsync()) {
			return callback.getCallbackFuture();
		}

		synchronized (callback) {
			if (!callback.isNotified()) {
				callback.setWaiting(true);
				// 如果是同步，挂起线程等待
				callback.wait(this.config.getRpcTimeout());
				callback.setWaiting(false);
			}
		}
		if (callback.getException() != null) {
			throw callback.getException();
		}

		return callback.getCallbackFuture().get();

	}

}
