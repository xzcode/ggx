package com.ggx.rpc.client.service.provider;

import java.util.List;
import java.util.Map;

import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.proxy.RpcProxyManager;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.RpcServiceClassCache;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.client.service.loadbalancer.impl.ConsistentHashingRpcServiceLoadblancer;
import com.ggx.rpc.common.constant.RpcServiceCustomDataKeys;
import com.ggx.rpc.common.model.InterfaceInfoModel;
import com.ggx.util.json.GGXJsonUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcServiceProvider extends ListenableMapDataManager<String, RpcServiceGroup>{
	
	protected RpcClientConfig config;

	public RpcServiceProvider(RpcClientConfig config) {
		this.config = config;
	}

	public void init() {
		RegistryClient registryClient = this.config.getRegistryClient();
		
		ServiceManager serviceManager = registryClient.getConfig().getServiceManager();

		serviceManager.addRegisterListener(service -> {
			registerRpcService(service);
		});

		// 添加注册中心服务管器的服务取消注册监听器
		serviceManager.addUnregisterListener(service -> {
			String serviceGroupId = service.getServiceGroupId();
			RpcServiceGroup rpcServiceGroup = this.get(serviceGroupId);
			rpcServiceGroup.remove(service.getServiceId());
		});

		serviceManager.addUpdateListener(service -> {
			String serviceGroupId = service.getServiceGroupId();
			RpcServiceGroup rpcServiceGroup = this.get(serviceGroupId);
			RpcService routerService = rpcServiceGroup.get(service.getServiceId());
			if (routerService != null) {
				routerService.addAllExtraData(service.getCustomData());
			} else {
				registerRpcService(service);
			}

		});
	}
	
	private void registerRpcService(ServiceInfo service) {
		
		//获取自定义参数
		Map<String, String> customData = service.getCustomData();
		
		String isRpcService = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE);
		if (isRpcService == null || isRpcService.isEmpty()) {
			return;
		}
		String servicePortString = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE_PORT);
		if (servicePortString == null) {
			return;
		}
		String interfaceInfosJson = customData.get(RpcServiceCustomDataKeys.RPC_INTERFACE_INFO_LIST);
		@SuppressWarnings("unchecked")
		List<InterfaceInfoModel> interfaceInfos = GGXJsonUtil.fromJson(interfaceInfosJson, List.class, InterfaceInfoModel.class);
		
		
		Integer servicePort = Integer.valueOf(servicePortString);
		
		String serviceGroupId = service.getServiceGroupId();
		
		String serviceId = service.getServiceId();
		
		RpcServiceGroup serviceGroup = this.get(serviceGroupId);
		boolean newGroup = false;
		if (serviceGroup != null) {
			//检查是否存在id一样的旧服务
			RpcService oldService = serviceGroup.get(serviceId);
			if (oldService != null) {
				if (servicePort != oldService.getPort()) {
					//移除端口信息不一致的旧服务
					serviceGroup.remove(serviceId);
				}
				else if (!oldService.isAvailable()) {
					//移除不可用的旧服务
					serviceGroup.remove(serviceId);
				}else {
					return;
				}
			}else {
				newGroup = true;
				serviceGroup = new RpcServiceGroup(serviceGroupId, new ConsistentHashingRpcServiceLoadblancer(serviceGroup));
				RpcServiceGroup fServiceGroup = serviceGroup;
				serviceGroup.onRemove(rpcService -> {
					rpcService.shutdown();
					//如果组内无服务，则移除组
					if (fServiceGroup.size() == 0) {
						this.remove(fServiceGroup.getServiceGroupId());
					}
				});
				this.put(serviceGroupId, serviceGroup);
				
			}
		}
		
		//创建新服务对象
		RpcService rpcService = new RpcService(config);
		rpcService.setServiceId(serviceId);
        rpcService.setHost(service.getHost());
        rpcService.setPort(servicePort);
        rpcService.setServiceId(service.getServiceId());
        rpcService.setServiceGroupId(service.getServiceGroupId());
        rpcService.setServiceName(service.getServiceName());
        rpcService.addAllExtraData(service.getCustomData());
        
        //添加到服务组
        serviceGroup.put(serviceId, rpcService);
        
        //如果是新创建的组，需要注册接口信息
        if (newGroup) {
	        RpcServiceClassCache classCache = this.config.getClassCache();
	        InterfaceServiceGroupCache interfaceServiceCache = this.config.getInterfaceServiceGroupCache();
	        RpcProxyManager proxyManager = this.config.getProxyManager();
			for (InterfaceInfoModel info : interfaceInfos) {
				String interfaceName = info.getInterfaceName();
				String fallbackClassName = info.getFallbackClassName();
				
				//注册并接口动态代理
				proxyManager.register(classCache.get(interfaceName),this.config.getFallbackInstanceFactory().instant(classCache.get(fallbackClassName)));
				
				//接口服务组缓存
				interfaceServiceCache.put(classCache.get(interfaceName), serviceGroup);
			}
			
			//当移除组的时候，移除接口服务组缓存
			this.onRemove(group -> {
				for (InterfaceInfoModel info : interfaceInfos) {
					interfaceServiceCache.remove(classCache.get(info.getInterfaceName()));
				}
			});
        }
        
        rpcService.init();
        
		
	}

}
