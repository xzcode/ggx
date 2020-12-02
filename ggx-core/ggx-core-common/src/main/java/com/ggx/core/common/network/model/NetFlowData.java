package com.ggx.core.common.network.model;

public class NetFlowData {
	
	private long upPerSecond;
	private long downPerSecond;
	private long totalUp;
	private long totalDown;
	private long total;
	private long lastUpTime;
	private long lastDownTime;
	
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
	
	public long getLastDownTime() {
		return lastDownTime;
	}
	
	public void setLastDownTime(long lastDownTime) {
		this.lastDownTime = lastDownTime;
	}
	
	public void setLastUpTime(long lastUpTime) {
		this.lastUpTime = lastUpTime;
	}
	
	public long getLastUpTime() {
		return lastUpTime;
	}

}
