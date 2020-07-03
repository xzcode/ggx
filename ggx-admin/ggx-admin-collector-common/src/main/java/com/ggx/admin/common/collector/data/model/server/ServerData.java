package com.ggx.admin.common.collector.data.model.server;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ServerData {
	
	//服务id
	private String serviceId;
	
	//操作系统信息
	private String os;
	
	//cpu信息
	private String cpu;
	
	//总内存
	private long totalMemory;
	
	//已使用的内存
	private long usedMemory;
	
	//总虚拟内存
	private long totalVirtualMemory;
	
	//已使用的虚拟内存
	private long usedVirtualMemory;
	
	//进程数
	private int processes;
	
	//线程数
	private int threads;
	
	//cpu使用率
	private double cpuUse;
	
	//文件系统存储信息
	private List<FileStoreInfo> fileStoreInfos;
	
	//网络信息
	private List<NetworkInfo> networkInfos;
	
	//自定义数据
	private Map<String, String> customData;
	
	//创建日期
	private Date createDate;

	public String getOs() {
		return os;
	}
	
	public void setOs(String os) {
		this.os = os;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	

	public long getTotalVirtualMemory() {
		return totalVirtualMemory;
	}

	public void setTotalVirtualMemory(long totalVirtualMemory) {
		this.totalVirtualMemory = totalVirtualMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public long getUsedVirtualMemory() {
		return usedVirtualMemory;
	}

	public void setUsedVirtualMemory(long usedVirtualMemory) {
		this.usedVirtualMemory = usedVirtualMemory;
	}

	public int getProcesses() {
		return processes;
	}

	public void setProcesses(int processes) {
		this.processes = processes;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public double getCpuUse() {
		return cpuUse;
	}

	public void setCpuUse(double cpuUse) {
		this.cpuUse = cpuUse;
	}

	public List<FileStoreInfo> getFileStoreInfos() {
		return fileStoreInfos;
	}

	public void setFileStoreInfos(List<FileStoreInfo> fileStoreInfos) {
		this.fileStoreInfos = fileStoreInfos;
	}

	public List<NetworkInfo> getNetworkInfos() {
		return networkInfos;
	}

	public void setNetworkInfos(List<NetworkInfo> networkInfos) {
		this.networkInfos = networkInfos;
	}

	public Map<String, String> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, String> customData) {
		this.customData = customData;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
}
