package com.ggx.admin.collector.client.collector;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.admin.collector.client.config.GGXAdminCollectorClientConfig;
import com.ggx.admin.common.collector.data.collector.DataCollector;
import com.ggx.admin.common.collector.data.model.server.FileStoreInfo;
import com.ggx.admin.common.collector.data.model.server.NetworkInfo;
import com.ggx.admin.common.collector.data.model.server.ServerData;
import com.ggx.core.common.executor.TaskExecutor;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

/**
 * 服务器系统信息收集器
 *
 * @author zai 2020-04-21 16:00:49
 */
public class ServerDataCollector implements DataCollector<ServerData> {

	protected GGXAdminCollectorClientConfig config;

	// 系统信息对象
	protected SystemInfo si = new SystemInfo();

	// 操作系统
	protected OperatingSystem os = si.getOperatingSystem();

	// 硬件抽象层
	protected HardwareAbstractionLayer hal = si.getHardware();;

	// 处理器
	protected CentralProcessor processor = hal.getProcessor();

	// 全局内存
	protected GlobalMemory memory = hal.getMemory();

	// jvm堆内存使用情况对象
	protected MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

	// jvm非堆内存使用情况对象
	protected MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

	// 线程信息管理对象
	protected OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
	// 线程信息管理对象
	protected ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

	// jvm cpu上次收集的计数
	protected long jvmCpuPrevTime;

	// jvm cpu上次收集的使用率
	protected long jvmCpuPrevUse;

	// jvm 内cpu使用率
	protected double jvmCpuUse;

	// 文件系统
	protected FileSystem fileSystem = os.getFileSystem();

	// 网络接口
	protected NetworkIF[] networkIFs = hal.getNetworkIFs();

	// 任务执行器
	protected TaskExecutor taskExecutor;

	// cpu信息
	protected String cpuInfo = this.getCpuInfo();

	// 文件系统信息集合
	protected List<FileStoreInfo> fileStoreInfos;;

	// 网络信息集合
	protected List<NetworkInfo> networkInfos;;

	// 上一个时间点的cpu时钟信息
	protected long[] prevTicks = processor.getSystemCpuLoadTicks();

	// cpu使用率
	protected double cpuUse;

	// 上一个时间点所有网卡上行总流量
	protected Map<String, Long> prevUploadTotalMap = new ConcurrentHashMap<>();

	// 上一个时间所有网卡点下行总流量
	protected Map<String, Long> prevDownloadTotalMap = new ConcurrentHashMap<>();

	public ServerDataCollector(GGXAdminCollectorClientConfig config) {
		this.config = config;
		this.taskExecutor = config.getTaskExecutor();
	}

	public ServerData collect() {
		
		//更新信息
		this.updateInfo();

		//包装数据
		ServerData serverData = new ServerData();
		serverData.setServiceId(this.config.getServiceId());
		serverData.setOs(os.toString());
		serverData.setCpu(cpuInfo);
		serverData.setTotalMemory(memory.getTotal());
		serverData.setUsedMemory(memory.getTotal() - memory.getAvailable());
		serverData.setTotalVirtualMemory(memory.getVirtualMemory().getSwapTotal());
		serverData.setUsedVirtualMemory(memory.getVirtualMemory().getSwapUsed());
		serverData.setThreads(os.getThreadCount());
		serverData.setProcesses(os.getProcessCount());

		serverData.setCpuUse(this.cpuUse);

		serverData.setNetworkInfos(this.networkInfos);

		serverData.setJvmHeapMemoryUsageUsed(heapMemoryUsage.getUsed());
		serverData.setJvmHeapMemoryUsageCommitted(heapMemoryUsage.getCommitted());
		serverData.setJvmHeapMemoryUsageInit(heapMemoryUsage.getInit());
		serverData.setJvmHeapMemoryUsageMax(heapMemoryUsage.getMax());

		serverData.setJvmNonheapMemoryUsageUsed(nonHeapMemoryUsage.getUsed());
		serverData.setJvmNonheapMemoryUsageCommitted(nonHeapMemoryUsage.getCommitted());
		serverData.setJvmNonheapMemoryUsageInit(nonHeapMemoryUsage.getInit());
		serverData.setJvmNonheapMemoryUsageMax(nonHeapMemoryUsage.getMax());

		return serverData;
	}

	public String getCpuInfo() {
		if (this.cpuInfo == null) {
			StringBuilder sb = new StringBuilder(128);
			sb.append(processor.toString()).append(" ").append(processor.getPhysicalPackageCount()).append(" CPU ")
					.append(processor.getPhysicalProcessorCount()).append(" Cores ")
					.append(processor.getLogicalProcessorCount()).append(" Threads");
			this.cpuInfo = sb.toString();
		}
		return cpuInfo;
	}

	/**
	 * 更新系统cpu使用信息
	 *
	 * @return
	 * @author zai 2020-04-22 15:24:07
	 */
	public long updateCpuUse() {
		long[] ticks = processor.getSystemCpuLoadTicks();

		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

		// cpu总使用率
		this.cpuUse = 100d * idle / totalCpu;

		this.prevTicks = ticks;
		return totalCpu;
	}

	/**
	 * 更新jvm cpu使用信息
	 *
	 * @return
	 * @author zai 2020-07-16 18:57:01
	 */
	public double updateJvmCpuUse() {
		long totalTime = 0;
		for (long id : threadMXBean.getAllThreadIds()) {
			totalTime += threadMXBean.getThreadCpuTime(id);
		}
		long curtime = System.nanoTime();
		long usedTime = totalTime - jvmCpuPrevUse;
		long totalPassedTime = curtime - jvmCpuPrevTime;
		jvmCpuPrevTime = curtime;
		jvmCpuPrevUse = totalTime;
		this.jvmCpuUse = (((double) usedTime) / totalPassedTime / osMXBean.getAvailableProcessors()) * 100;
		return this.jvmCpuUse;
	}

	/**
	 * 获取文件系统存储信息
	 *
	 * @return
	 * @author zai 2020-04-21 18:37:59
	 */
	public List<FileStoreInfo> updateFileStoreInfos() {
		List<FileStoreInfo> fileStoreInfos = new ArrayList<>();
		OSFileStore[] fsArray = fileSystem.getFileStores();
		for (OSFileStore fs : fsArray) {
			FileStoreInfo info = new FileStoreInfo();
			info.setName(fs.getLogicalVolume() == null ? fs.getName() : fs.getLogicalVolume());
			info.setTotalSpace(fs.getTotalSpace());
			info.setUsableSpace(fs.getUsableSpace());
			fileStoreInfos.add(info);
		}

		this.fileStoreInfos = fileStoreInfos;

		return fileStoreInfos;
	}

	/**
	 * 更新网络信息
	 *
	 * @return
	 * @author zai 2020-04-22 14:03:25
	 */
	public List<NetworkInfo> updateNetworkInfos() {
		List<NetworkInfo> networkInfos = new ArrayList<>();

		for (NetworkIF net : networkIFs) {
			NetworkInfo info = new NetworkInfo();
			String name = net.getName();
			info.setName(name);
			info.setIpv4(Arrays.toString(net.getIPv4addr()));
			info.setIpv6(Arrays.toString(net.getIPv6addr()));
			info.setMtu(net.getMTU());
			info.setMac(net.getMacaddr());

			long bytesRecv = net.getBytesRecv();
			long bytesSent = net.getBytesSent();

			Long prevBytesSent = this.prevUploadTotalMap.get(name);
			if (prevBytesSent == null) {
				info.setCurrentUpload(0L);
			} else {
				info.setCurrentUpload(bytesSent - prevBytesSent);
			}
			this.prevUploadTotalMap.put(name, bytesSent);

			Long prevBytesRecv = this.prevDownloadTotalMap.get(name);
			if (prevBytesRecv == null) {
				info.setCurrentDownload(0L);
			} else {
				info.setCurrentDownload(bytesRecv - prevBytesRecv);
			}
			this.prevDownloadTotalMap.put(name, bytesRecv);

			networkInfos.add(info);
		}

		this.networkInfos = networkInfos;

		return networkInfos;
	}


	public void updateInfo() {
		// 更新cpu使用率信息
		this.updateCpuUse();
		
		// 更新jvm cpu使用率信息
		this.updateJvmCpuUse();

		// 更新文件系统信息
		this.updateFileStoreInfos();

		// 更新网络信息
		this.updateNetworkInfos();
	}

}
