package com.ggx.data.collector.osinfo;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ggx.data.collector.DataCollector;
import com.ggx.data.collector.osinfo.model.JvmInfo;
import com.ggx.data.collector.osinfo.model.JvmThreadInfo;
import com.ggx.data.collector.osinfo.model.NetworkInfo;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

/**
 * 服务器系统信息收集器
 *
 * @author zai 2020-04-21 16:00:49
 */
public class GGXOsInfoCollector implements DataCollector<JvmInfo> {

	// 系统信息对象
	protected SystemInfo si = new SystemInfo();

	// 硬件抽象层
	protected HardwareAbstractionLayer hal = si.getHardware();;



	// jvm堆内存使用情况对象
	protected MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();

	// jvm非堆内存使用情况对象
	protected MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

	// 线程信息管理对象
	protected OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

	// 线程信息管理对象
	protected ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

	// 运行时管理对象
	protected RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

	// jvm参数
	protected String jvmArgs = runtimeMXBean.getInputArguments().stream().collect(Collectors.joining(","));;

	// jvm cpu上次收集的计数
	protected long jvmCpuPrevTime;

	// jvm cpu上次收集的使用率
	protected long jvmCpuPrevUse;
	
	// jvm 内cpu使用率
	protected double jvmCpuUse;

	// 可用处理器个数
	protected int availableProcessors = osMXBean.getAvailableProcessors();
	
	// pid
	protected String pid = getPid();

	// 网络接口
	protected NetworkIF[] networkIFs = hal.getNetworkIFs();

	// 网络信息集合
	protected List<NetworkInfo> networkInfos;

	// 上一个时间点所有网卡上行总流量
	protected Map<String, Long> prevUploadTotalMap = new ConcurrentHashMap<>();

	// 上一个时间所有网卡点下行总流量
	protected Map<String, Long> prevDownloadTotalMap = new ConcurrentHashMap<>();

	public JvmInfo collect() {

		// 更新信息
		this.updateInfo();

		// 包装数据
		JvmInfo jvmInfo = new JvmInfo();
		
		jvmInfo.setAvailableProcessors(osMXBean.getAvailableProcessors());
		jvmInfo.setJvmUpTime(runtimeMXBean.getUptime());
		jvmInfo.setJvmThreads(threadMXBean.getThreadCount());
		jvmInfo.setJvmArgs(jvmArgs);

		jvmInfo.setJvmHeapMemoryUsageUsed(heapMemoryUsage.getUsed());
		jvmInfo.setJvmHeapMemoryUsageCommitted(heapMemoryUsage.getCommitted());
		jvmInfo.setJvmHeapMemoryUsageInit(heapMemoryUsage.getInit());
		jvmInfo.setJvmHeapMemoryUsageMax(heapMemoryUsage.getMax());

		jvmInfo.setJvmNonheapMemoryUsageUsed(nonHeapMemoryUsage.getUsed());
		jvmInfo.setJvmNonheapMemoryUsageCommitted(nonHeapMemoryUsage.getCommitted());
		jvmInfo.setJvmNonheapMemoryUsageInit(nonHeapMemoryUsage.getInit());
		jvmInfo.setJvmNonheapMemoryUsageMax(nonHeapMemoryUsage.getMax());

		return jvmInfo;
	}

	public List<NetworkInfo> getNetworkInfos() {
		return networkInfos;
	}

	public List<JvmThreadInfo> getJvmThreadInfos() {

		List<JvmThreadInfo> jvmThreadInfos = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (long id : threadMXBean.getAllThreadIds()) {
			ThreadInfo threadInfo = threadMXBean.getThreadInfo(id);
			JvmThreadInfo jvmThreadInfo = new JvmThreadInfo();
			jvmThreadInfo.setThreadName(threadInfo.getThreadName());
			jvmThreadInfo.setSuspended(threadInfo.isSuspended());
			jvmThreadInfo.setThreadState(threadInfo.getThreadState().toString());
			jvmThreadInfo.setBlockedTime(threadInfo.getBlockedTime());
			jvmThreadInfo.setBlockedCount(threadInfo.getBlockedCount());
			jvmThreadInfo.setWaitedTime(threadInfo.getBlockedTime());
			jvmThreadInfo.setWaitedCount(threadInfo.getWaitedCount());
			jvmThreadInfo.setLockName(threadInfo.getLockName());
			jvmThreadInfo.setLockOwnerName(threadInfo.getLockOwnerName());
			jvmThreadInfo.setInNative(threadInfo.isInNative());

			StackTraceElement[] stackTrace = threadInfo.getStackTrace();

			for (StackTraceElement element : stackTrace) {
				sb.append(element.toString()).append(",\n");
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			jvmThreadInfo.setThreadTrace(sb.toString());
			sb.setLength(0);

			jvmThreadInfos.add(jvmThreadInfo);
		}
		return jvmThreadInfos;
	}

	/**
	 * 更新jvm cpu使用信息
	 *
	 * @return
	 * @author zai 2020-07-16 18:57:01
	 */
	public double updateJvmCpuUse() {
		long totalCpuTime = 0;
		for (long id : threadMXBean.getAllThreadIds()) {
			totalCpuTime += threadMXBean.getThreadCpuTime(id);
		}
		
		long curtime = System.nanoTime();
		long usedTime = totalCpuTime - jvmCpuPrevUse;
		long totalPassedTime = curtime - jvmCpuPrevTime;
		jvmCpuPrevTime = curtime;
		jvmCpuPrevUse = totalCpuTime;
		this.jvmCpuUse = (((double) usedTime) / totalPassedTime / availableProcessors) * 100;
		return this.jvmCpuUse;
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

		// 更新jvm cpu使用率信息
		this.updateJvmCpuUse();

		// 更新网络信息
		this.updateNetworkInfos();
	}

	private String getPid() {
		String pid = ManagementFactory.getRuntimeMXBean().getName();

		int indexOf = pid.indexOf('@');
		if (indexOf > 0) {
			pid = pid.substring(0, indexOf);
		}
		return pid;
	}

}
