package com.ggx.spring.common.base.util;

/**
 * 玩家编号工具类
 * 
 * @author zai
 * 2019-03-19 15:19:13
 */
public class PlayerNoUtil {
	
	/**
	 * 隐藏玩家编号
	 * 
	 * @param playerNo
	 * @param showLength 显示的剩余长度
	 * @return
	 * @author zai
	 * 2019-03-19 15:19:22
	 */
	public static String hidePlayerNo(String playerNo, int showLength) {
		if (playerNo == null) {
			return playerNo;
		}
		char[] arr = new char[playerNo.length()];
		for (int i = 0; i < arr.length; i++) {
			if (i < playerNo.length() - showLength) {
				arr[i] = '*';
			}else {
				arr[i] = playerNo.charAt(i);
			}
		}
		return new String(arr);
		
	}
	
	/**
	 * 隐藏玩家编号
	 * 
	 * @param playerNo
	 * @return
	 * @author zai
	 * 2019-03-19 15:19:55
	 */
	public static String hidePlayerNo(String playerNo) {
		return hidePlayerNo(playerNo, 3);
	}
	

}
