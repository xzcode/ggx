package com.ggx.admin.server.handler.services.model.resp;

import java.util.ArrayList;
import java.util.List;

public class ServiceModel {
	
		//服务id
		protected String serviceId;
		
		//服务组id
		protected String serviceGroupId;
		
		//服务名称
		protected String serviceName;
		
		//服务ip地址
		protected String host;
		
		//所在地区
		protected String region = "default";
			
		//所在分区
		protected String zone = "default";
		
		//自定义数据
		protected List<CustomData> customData = new ArrayList<>();

		/**
		 * 添加自定义参数
		 * 
		 * @param key
		 * @param value
		 * @author zai
		 * 2020-02-04 11:19:05
		 */
		public void addCustomData(String key, String value) {
			customData.add(new CustomData(key, value));
		}
		
		
		
		public String getServiceGroupId() {
			return serviceGroupId;
		}
		public void setServiceGroupId(String serviceName) {
			this.serviceGroupId = serviceName;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String ip) {
			this.host = ip;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}
		
		public List<CustomData> getCustomData() {
			return customData;
		}
		
		public void setCustomData(List<CustomData> customData) {
			this.customData = customData;
		}
		
		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getZone() {
			return zone;
		}

		public void setZone(String zone) {
			this.zone = zone;
		}
		
		public String getServiceName() {
			return serviceName;
		}
		
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		

}
