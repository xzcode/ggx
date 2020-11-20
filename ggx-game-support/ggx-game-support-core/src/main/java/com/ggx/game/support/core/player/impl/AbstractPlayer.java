package com.ggx.game.support.core.player.impl;

import com.ggx.core.common.session.GGXSession;
import com.ggx.game.support.core.player.GGXPlayer;

/**
     玩家基类
 * 
 * @author zai
 * 2019-01-21 20:21:20
 */
public abstract class AbstractPlayer implements GGXPlayer{
	
	protected GGXSession session; 
	
	//玩家id
	protected String playerId; 
	
	
	public String getPlayerId() {
		return playerId;
	}


	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}


	@Override
	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
	}


	
}
