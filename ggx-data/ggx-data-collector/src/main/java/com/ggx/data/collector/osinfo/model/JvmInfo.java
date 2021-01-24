package com.ggx.data.collector.osinfo.model;

import java.util.Date;

public class JvmInfo {
	
	// 服务id
	private String serviceId;

	// 进程id
	private String pid;

	
	// 可用的处理器个数
	private int availableProcessors;
	
	// jvm运行时长
	private long jvmUpTime;
	
	
	// jvm启动参数
	private String jvmArgs;
	
	// jvm线程数
	private long jvmThreads;

	// jvm 内cpu使用率
	protected double jvmCpuUsage;

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

	public double getJvmCpuUsage() {
		return jvmCpuUsage;
	}

	public void setJvmCpuUsage(double jvmCpuUse) {
		this.jvmCpuUsage = jvmCpuUse;
	}

	public String getJvmArgs() {
		return jvmArgs;
	}

	public void setJvmArgs(String jvmArgs) {
		this.jvmArgs = jvmArgs;
	}

	public long getJvmThreads() {
		return jvmThreads;
	}

	public void setJvmThreads(long jvmThreads) {
		this.jvmThreads = jvmThreads;
	}
	
	public long getJvmUpTime() {
		return jvmUpTime;
	}
	
	public void setJvmUpTime(long jvmUpTime) {
		this.jvmUpTime = jvmUpTime;
	}
	public int getAvailableProcessors() {
		return availableProcessors;
	}
	
	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
}
