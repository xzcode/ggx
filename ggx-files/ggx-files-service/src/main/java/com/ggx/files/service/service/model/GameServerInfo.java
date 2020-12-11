package com.ggx.files.service.service.model;

public class GameServerInfo {
	
	
	private String serverNo;
	
	private String serverName;
	
	private int onlineUsers;
	
	private int loadStatus;
	
	private boolean recommend;
	
	private boolean newServer;
	
	private int status;
	
	private boolean registered;
	
	public String getServerNo() {
		return serverNo;
	}

	public void setServerNo(String serverNo) {
		this.serverNo = serverNo;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(int onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public int getLoadStatus() {
		return loadStatus;
	}

	public void setLoadStatus(int loadStatus) {
		this.loadStatus = loadStatus;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public boolean isNewServer() {
		return newServer;
	}

	public void setNewServer(boolean newServer) {
		this.newServer = newServer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}
