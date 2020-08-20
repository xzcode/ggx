package com.ggx.admin.server.handler.services.model.resp;

import com.ggx.admin.common.collector.data.model.server.ServerData;

public class ServerDataModel {

	// 服务id
	private String serviceId;

	// 操作系统信息
	private String os;

	// cpu信息
	private String cpu;

	// 总内存
	private Long totalMemory;

	// 已使用的内存
	private Long usedMemory;

	// 总虚拟内存
	private Long totalVirtualMemory;

	// 已使用的虚拟内存
	private Long usedVirtualMemory;

	// 进程数
	private int processes;

	// 线程数
	private int threads;

	// cpu使用率
	private Double cpuUse;

	// jvm 内cpu使用率
	private Double jvmCpuUse;

	// 当前上行速度 bytes/s
	private Long networkTotalUpload;

	// 当前下行发送速度 bytes/s
	private Long networkTotalDownload;

	// 初始化堆内存
	private Long jvmHeapMemoryUsageInit;

	// 已使用堆内存
	private Long jvmHeapMemoryUsageUsed;

	// 可使用堆内存
	private Long jvmHeapMemoryUsageCommitted;

	// 最大堆内存
	private Long jvmHeapMemoryUsageMax;

	// 初始化非堆内存
	private Long jvmNonheapMemoryUsageInit;

	// 已使用非堆内存
	private Long jvmNonheapMemoryUsageUsed;

	// 可使用非堆内存
	private Long jvmNonheapMemoryUsageCommitted;

	// 最大非堆内存
	private Long jvmNonheapMemoryUsageMax;

	// 创建日期
	private Long createTime;
	
	
	public static ServerDataModel create(ServerData serverData) {
		
		ServerDataModel serverDataModel = new ServerDataModel();
		
		serverDataModel.setServiceId(serverData.getServiceId());

		serverDataModel.setOs(serverData.getOs());

		serverDataModel. setCpu(serverData.getCpu());

		serverDataModel. setTotalMemory(serverData.getTotalMemory());

		serverDataModel. setUsedMemory(serverData.getUsedMemory());

		serverDataModel. setTotalVirtualMemory(serverData.getTotalVirtualMemory());

		serverDataModel. setUsedVirtualMemory(serverData.getUsedVirtualMemory());

		serverDataModel. setProcesses(serverData.getProcesses());

		serverDataModel. setThreads(serverData.getThreads());

		serverDataModel. setCpuUse(serverData.getCpuUse());

		serverDataModel. setJvmCpuUse(serverData.getJvmCpuUse());

		// 当前上行速度 bytes/s
		//serverDataModel. setNetworkTotalUpload(serverData.getNetworkInfos());

		// 当前下行发送速度 bytes/s
		//serverDataModel. setNetworkTotalDownload(networkTotalDownload);

		serverDataModel. setJvmHeapMemoryUsageInit(serverData.getJvmHeapMemoryUsageInit());

		serverDataModel. setJvmHeapMemoryUsageUsed(serverData.getJvmHeapMemoryUsageUsed());

		serverDataModel. setJvmHeapMemoryUsageCommitted(serverData.getJvmHeapMemoryUsageCommitted());

		serverDataModel. setJvmHeapMemoryUsageMax(serverData.getJvmHeapMemoryUsageMax());

		serverDataModel. setJvmNonheapMemoryUsageInit(serverData.getJvmNonheapMemoryUsageInit());

		serverDataModel. setJvmNonheapMemoryUsageUsed(serverData.getJvmNonheapMemoryUsageUsed());

		serverDataModel. setJvmNonheapMemoryUsageCommitted(serverData.getJvmNonheapMemoryUsageCommitted());

		serverDataModel. setJvmNonheapMemoryUsageMax(serverData.getJvmNonheapMemoryUsageMax());

		serverDataModel. setCreateTime(serverData.getCreateDate().getTime());
		
		return serverDataModel;
		
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

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

	public Long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Long getTotalVirtualMemory() {
		return totalVirtualMemory;
	}

	public void setTotalVirtualMemory(Long totalVirtualMemory) {
		this.totalVirtualMemory = totalVirtualMemory;
	}

	public Long getUsedVirtualMemory() {
		return usedVirtualMemory;
	}

	public void setUsedVirtualMemory(Long usedVirtualMemory) {
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

	public Double getCpuUse() {
		return cpuUse;
	}

	public void setCpuUse(Double cpuUse) {
		this.cpuUse = cpuUse;
	}

	public Double getJvmCpuUse() {
		return jvmCpuUse;
	}

	public void setJvmCpuUse(Double jvmCpuUse) {
		this.jvmCpuUse = jvmCpuUse;
	}

	public Long getNetworkTotalUpload() {
		return networkTotalUpload;
	}

	public void setNetworkTotalUpload(Long networkTotalUpload) {
		this.networkTotalUpload = networkTotalUpload;
	}

	public Long getNetworkTotalDownload() {
		return networkTotalDownload;
	}

	public void setNetworkTotalDownload(Long networkTotalDownload) {
		this.networkTotalDownload = networkTotalDownload;
	}

	public Long getJvmHeapMemoryUsageInit() {
		return jvmHeapMemoryUsageInit;
	}

	public void setJvmHeapMemoryUsageInit(Long jvmHeapMemoryUsageInit) {
		this.jvmHeapMemoryUsageInit = jvmHeapMemoryUsageInit;
	}

	public Long getJvmHeapMemoryUsageUsed() {
		return jvmHeapMemoryUsageUsed;
	}

	public void setJvmHeapMemoryUsageUsed(Long jvmHeapMemoryUsageUsed) {
		this.jvmHeapMemoryUsageUsed = jvmHeapMemoryUsageUsed;
	}

	public Long getJvmHeapMemoryUsageCommitted() {
		return jvmHeapMemoryUsageCommitted;
	}

	public void setJvmHeapMemoryUsageCommitted(Long jvmHeapMemoryUsageCommitted) {
		this.jvmHeapMemoryUsageCommitted = jvmHeapMemoryUsageCommitted;
	}

	public Long getJvmHeapMemoryUsageMax() {
		return jvmHeapMemoryUsageMax;
	}

	public void setJvmHeapMemoryUsageMax(Long jvmHeapMemoryUsageMax) {
		this.jvmHeapMemoryUsageMax = jvmHeapMemoryUsageMax;
	}

	public Long getJvmNonheapMemoryUsageInit() {
		return jvmNonheapMemoryUsageInit;
	}

	public void setJvmNonheapMemoryUsageInit(Long jvmNonheapMemoryUsageInit) {
		this.jvmNonheapMemoryUsageInit = jvmNonheapMemoryUsageInit;
	}

	public Long getJvmNonheapMemoryUsageUsed() {
		return jvmNonheapMemoryUsageUsed;
	}

	public void setJvmNonheapMemoryUsageUsed(Long jvmNonheapMemoryUsageUsed) {
		this.jvmNonheapMemoryUsageUsed = jvmNonheapMemoryUsageUsed;
	}

	public Long getJvmNonheapMemoryUsageCommitted() {
		return jvmNonheapMemoryUsageCommitted;
	}

	public void setJvmNonheapMemoryUsageCommitted(Long jvmNonheapMemoryUsageCommitted) {
		this.jvmNonheapMemoryUsageCommitted = jvmNonheapMemoryUsageCommitted;
	}

	public Long getJvmNonheapMemoryUsageMax() {
		return jvmNonheapMemoryUsageMax;
	}

	public void setJvmNonheapMemoryUsageMax(Long jvmNonheapMemoryUsageMax) {
		this.jvmNonheapMemoryUsageMax = jvmNonheapMemoryUsageMax;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	

}
