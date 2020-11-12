package com.ggx.rpc.client.service.provider;

import java.util.List;
import java.util.Map;

import com.ggx.registry.client.RegistryClient;
import com.ggx.registry.common.service.ServiceInfo;
import com.ggx.registry.common.service.ServiceManager;
import com.ggx.rpc.client.config.RpcClientConfig;
import com.ggx.rpc.client.service.InterfaceServiceGroupCache;
import com.ggx.rpc.client.service.RpcService;
import com.ggx.rpc.client.service.RpcServiceClassCache;
import com.ggx.rpc.client.service.group.RpcServiceGroup;
import com.ggx.rpc.client.service.loadbalancer.impl.ConsistentHashingRpcServiceLoadbalancer;
import com.ggx.rpc.common.constant.RpcServiceCustomDataKeys;
import com.ggx.rpc.common.model.InterfaceInfoModel;
import com.ggx.util.json.GGXJsonUtil;
import com.ggx.util.manager.impl.ListenableMapDataManager;

public class RpcServiceProvider extends ListenableMapDataManager<String, RpcServiceGroup> {

	protected RpcClientConfig config;

	public RpcServiceProvider(RpcClientConfig config) {
		this.config = config;
		this.init();
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
			if (rpcServiceGroup != null) {
				rpcServiceGroup.remove(service.getServiceId());
			}
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

	@SuppressWarnings("unchecked")
	private void registerRpcService(ServiceInfo service) {

		// 获取自定义参数
		Map<String, String> customData = service.getCustomData();

		String isRpcService = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE);
		if (isRpcService == null || isRpcService.isEmpty()) {
			return;
		}
		String rpcServiceGroupId = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE_GROUP_ID);
		if (rpcServiceGroupId == null || rpcServiceGroupId.isEmpty()) {
			return;
		}

		// 跳过同组注册信息
		if (rpcServiceGroupId.equals(config.getRpcServiceGroupId())) {
			return;
		}

		String rpcServiceId = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE_ID);
		if (rpcServiceId == null || rpcServiceId.isEmpty()) {
			return;
		}

		String servicePortString = customData.get(RpcServiceCustomDataKeys.RPC_SERVICE_PORT);
		if (servicePortString == null) {
			return;
		}
		String interfaceInfosJson = customData.get(RpcServiceCustomDataKeys.RPC_INTERFACE_INFO_LIST);

		List<InterfaceInfoModel> interfaceInfos = GGXJsonUtil.fromJson(interfaceInfosJson, List.class,
				InterfaceInfoModel.class);

		Integer rpcServicePort = Integer.valueOf(servicePortString);

		InterfaceServiceGroupCache interfaceServiceCache = this.config.getInterfaceServiceGroupCache();
		RpcServiceClassCache classCache = this.config.getClassCache();
		RpcServiceGroup serviceGroup = this.get(rpcServiceGroupId);
		boolean newGroup = false;
		if (serviceGroup != null) {
			// 检查是否存在id一样的旧服务
			RpcService oldService = serviceGroup.get(rpcServiceId);
			if (oldService != null) {
				if (rpcServicePort != oldService.getPort()) {
					// 移除端口信息不一致的旧服务
					serviceGroup.remove(rpcServiceId);
				} else if (!oldService.isAvailable()) {
					// 移除不可用的旧服务
					serviceGroup.remove(rpcServiceId);
				} else {
					return;
				}
			}
		} else {
			newGroup = true;
			serviceGroup = new RpcServiceGroup(rpcServiceGroupId);
			serviceGroup.setLoadblancer(new ConsistentHashingRpcServiceLoadbalancer(serviceGroup));
			RpcServiceGroup fServiceGroup = serviceGroup;
			serviceGroup.onRemove(rpcService -> {
				rpcService.shutdown();
				// 如果组内无服务，则移除组
				if (fServiceGroup.size() == 0) {
					this.remove(fServiceGroup.getServiceGroupId());
					for (InterfaceInfoModel info : interfaceInfos) {
						interfaceServiceCache.remove(classCache.get(info.getInterfaceName()));
					}
				}
			});
			this.put(rpcServiceGroupId, serviceGroup);

		}

		// 创建新服务对象
		RpcService rpcService = new RpcService(config);
		rpcService.setServiceGroupId(rpcServiceGroupId);
		rpcService.setServiceId(rpcServiceId);
		rpcService.setPort(rpcServicePort);
		rpcService.setHost(service.getHost());
		rpcService.setServiceName(service.getServiceName());
		rpcService.addAllExtraData(service.getCustomData());

		// 添加到服务组
		serviceGroup.put(rpcServiceId, rpcService);

		// 如果是新创建的组，需要注册接口信息
		if (newGroup) {

			for (InterfaceInfoModel info : interfaceInfos) {
				String interfaceName = info.getInterfaceName();
				Class<?> interfaceClass = classCache.get(interfaceName);
				if (interfaceClass != null) {
					// 接口服务组缓存
					interfaceServiceCache.put(interfaceClass, serviceGroup);
				}
			}

		}

		rpcService.init();

	}

}
