package com.ggx.data.collector.osinfo.model;

import java.util.Date;

public class OSInfo {

	// 服务id
	private String serviceId;

	// 操作系统信息
	private String os;

	// cpu信息
	private String cpu;

	// 总内存
	private long totalMemory;

	// 已使用的内存
	private long usedMemory;

	// 总虚拟内存
	private long totalVirtualMemory;

	// 已使用的虚拟内存
	private long usedVirtualMemory;

	// 进程数
	private int processes;

	// 线程数
	private int threads;

	// cpu使用率
	private double cpuUse;
	
	// jvm 内cpu使用率
	protected double jvmCpuUse;

	// 初始化堆内存
	private long jvmHeapMemoryUsageInit;

	// 已使用堆内存
	private long jvmHeapMemoryUsageUsed;

	// 可使用堆内存
	private long jvmHeapMemoryUsageCommitted;

	// 最大堆内存
	private long jvmHeapMemoryUsageMax;

	// 初始化非堆内存
	private long jvmNonheapMemoryUsageInit;

	// 已使用非堆内存
	private long jvmNonheapMemoryUsageUsed;

	// 可使用非堆内存
	private long jvmNonheapMemoryUsageCommitted;

	// 最大非堆内存
	private long jvmNonheapMemoryUsageMax;

	// 创建日期
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

	public long getJvmHeapMemoryUsageInit() {
		return jvmHeapMemoryUsageInit;
	}

	public void setJvmHeapMemoryUsageInit(long jvmHeapMemoryUsageInit) {
		this.jvmHeapMemoryUsageInit = jvmHeapMemoryUsageInit;
	}

	public long getJvmHeapMemoryUsageUsed() {
		return jvmHeapMemoryUsageUsed;
	}

	public void setJvmHeapMemoryUsageUsed(long jvmHeapMemoryUsageUsed) {
		this.jvmHeapMemoryUsageUsed = jvmHeapMemoryUsageUsed;
	}

	public long getJvmHeapMemoryUsageCommitted() {
		return jvmHeapMemoryUsageCommitted;
	}

	public void setJvmHeapMemoryUsageCommitted(long jvmHeapMemoryUsageCommitted) {
		this.jvmHeapMemoryUsageCommitted = jvmHeapMemoryUsageCommitted;
	}

	public long getJvmHeapMemoryUsageMax() {
		return jvmHeapMemoryUsageMax;
	}

	public void setJvmHeapMemoryUsageMax(long jvmHeapMemoryUsageMax) {
		this.jvmHeapMemoryUsageMax = jvmHeapMemoryUsageMax;
	}

	public long getJvmNonheapMemoryUsageInit() {
		return jvmNonheapMemoryUsageInit;
	}

	public void setJvmNonheapMemoryUsageInit(long jvmNonheapMemoryUsageInit) {
		this.jvmNonheapMemoryUsageInit = jvmNonheapMemoryUsageInit;
	}

	public long getJvmNonheapMemoryUsageUsed() {
		return jvmNonheapMemoryUsageUsed;
	}

	public void setJvmNonheapMemoryUsageUsed(long jvmNonheapMemoryUsageUsed) {
		this.jvmNonheapMemoryUsageUsed = jvmNonheapMemoryUsageUsed;
	}

	public long getJvmNonheapMemoryUsageCommitted() {
		return jvmNonheapMemoryUsageCommitted;
	}

	public void setJvmNonheapMemoryUsageCommitted(long jvmNonheapMemoryUsageCommitted) {
		this.jvmNonheapMemoryUsageCommitted = jvmNonheapMemoryUsageCommitted;
	}

	public long getJvmNonheapMemoryUsageMax() {
		return jvmNonheapMemoryUsageMax;
	}

	public void setJvmNonheapMemoryUsageMax(long jvmNonheapMemoryUsageMax) {
		this.jvmNonheapMemoryUsageMax = jvmNonheapMemoryUsageMax;
	}

	public double getJvmCpuUse() {
		return jvmCpuUse;
	}

	public void setJvmCpuUse(double jvmCpuUse) {
		this.jvmCpuUse = jvmCpuUse;
	}

}
