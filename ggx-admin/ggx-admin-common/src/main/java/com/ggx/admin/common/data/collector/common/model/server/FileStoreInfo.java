package com.ggx.admin.common.data.collector.common.model.server;

public class FileStoreInfo {
	
	//文件目录名称
	protected String name;
	
	//可用空间
	protected long usableSpace;
	
	//总空间
	protected long totalSpace;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUsableSpace() {
		return usableSpace;
	}

	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}
	
	
}
