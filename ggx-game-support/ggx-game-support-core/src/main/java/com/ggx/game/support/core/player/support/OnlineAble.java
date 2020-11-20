package com.ggx.game.support.core.player.support;

/**
 * 在线接口
 * 
 * @author zai
 * 2020-9-14 21:58:16
 */
public interface OnlineAble {
	
	/**
	 * 是否在线
	 * @return
	 * @author zai
	 * 2020-9-14 21:57:37
	 */
	boolean isOnline();
	
	/**
	 * 设置是否在线
	 * @param online
	 * @author zai
	 * 2020-9-14 22:28:51
	 */
	void setOnline(boolean online);
	
}
