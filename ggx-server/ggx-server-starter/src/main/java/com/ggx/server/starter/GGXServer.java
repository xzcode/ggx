package com.ggx.server.starter;

import com.ggx.server.starter.constant.GGXServerMode;

public class GGXServer implements GGXServerStarter{
	
	private GGXServerStarter serverStarter;
	
	private String mode = GGXServerMode.CORE_SERVER;

	public GGXServer(String mode) {
		this.mode = mode;
	}

	public GGXServer() {
	}
	
	private void init() {
		switch (mode) {
		case GGXServerMode.CORE_SERVER:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void start() {
		serverStarter.start();
	}

	@Override
	public void shutdown() {
		serverStarter.shutdown();
	}
	
	
	

}
