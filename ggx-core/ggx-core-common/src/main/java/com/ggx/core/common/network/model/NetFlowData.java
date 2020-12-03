package com.ggx.core.common.network.model;

public class NetFlowData {
	
	private long upPerSecond;
	private long downPerSecond;
	private long totalPerSecond;
	private long totalUp;
	private long lastTotalUp;
	private long totalDown;
	private long lastTotalDown;
	private long total;
	private long lastTotal;
	private long createTime = System.currentTimeMillis();
	
	public void incrTotalPerSecond(long add) {
		this.totalPerSecond += add;
	}
	public void incrUpPerSecond(long add) {
		this.upPerSecond += add;
	}
	
	public void incrDownPerSecond(long add) {
		this.downPerSecond += add;
	}
	
	public void incrTotalUp(long add) {
		this.totalUp += add;
	}
	public void incrTotalDown(long add) {
		this.totalDown += add;
	}
	
	public void incrTotal(long add) {
		this.total += add;
	}
	
	public long getUpPerSecond() {
		return upPerSecond;
	}
	public void setUpPerSecond(long upPerSecond) {
		this.upPerSecond = upPerSecond;
	}
	public long getDownPerSecond() {
		return downPerSecond;
	}
	public void setDownPerSecond(long downPerSecond) {
		this.downPerSecond = downPerSecond;
	}
	public long getTotalUp() {
		return totalUp;
	}
	public void setTotalUp(long totalUp) {
		this.totalUp = totalUp;
	}
	public long getTotalDown() {
		return totalDown;
	}
	public void setTotalDown(long totalDown) {
		this.totalDown = totalDown;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
	
	public long getCreateTime() {
		return createTime;
	}

	public long getLastTotalUp() {
		return lastTotalUp;
	}

	public void setLastTotalUp(long lastTotalUp) {
		this.lastTotalUp = lastTotalUp;
	}

	public long getLastTotalDown() {
		return lastTotalDown;
	}

	public void setLastTotalDown(long lastTotalDown) {
		this.lastTotalDown = lastTotalDown;
	}

	public long getLastTotal() {
		return lastTotal;
	}

	public void setLastTotal(long lastTotal) {
		this.lastTotal = lastTotal;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getTotalPerSecond() {
		return totalPerSecond;
	}
	
	public void setTotalPerSecond(long totalPerSecond) {
		this.totalPerSecond = totalPerSecond;
	}
	
}
