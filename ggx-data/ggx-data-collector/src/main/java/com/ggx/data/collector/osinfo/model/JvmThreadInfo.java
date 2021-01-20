package com.ggx.data.collector.osinfo.model;

/**
 * 网络（网卡）信息
 *
 * @author zai 2020-04-22 14:12:40
 */
public class JvmThreadInfo {

	private String threadName;
	private boolean suspended;
	private String threadState; // Thread.State
	private long blockedTime;
	private long blockedCount;
	private long waitedTime;
	private long waitedCount;
	private String lockName;
	private String lockOwnerName;
	private boolean inNative;
	private String threadTrace;
	
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	public String getThreadState() {
		return threadState;
	}
	public void setThreadState(String threadState) {
		this.threadState = threadState;
	}
	public long getBlockedTime() {
		return blockedTime;
	}
	public void setBlockedTime(long blockedTime) {
		this.blockedTime = blockedTime;
	}
	public long getBlockedCount() {
		return blockedCount;
	}
	public void setBlockedCount(long blockedCount) {
		this.blockedCount = blockedCount;
	}
	public long getWaitedTime() {
		return waitedTime;
	}
	public void setWaitedTime(long waitedTime) {
		this.waitedTime = waitedTime;
	}
	public long getWaitedCount() {
		return waitedCount;
	}
	public void setWaitedCount(long waitedCount) {
		this.waitedCount = waitedCount;
	}
	public String getLockName() {
		return lockName;
	}
	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
	public String getLockOwnerName() {
		return lockOwnerName;
	}
	public void setLockOwnerName(String lockOwnerName) {
		this.lockOwnerName = lockOwnerName;
	}
	public boolean isInNative() {
		return inNative;
	}
	public void setInNative(boolean inNative) {
		this.inNative = inNative;
	}
	
	public void setThreadTrace(String threadTrace) {
		this.threadTrace = threadTrace;
	}
	
	public String getThreadTrace() {
		return threadTrace;
	}

}
