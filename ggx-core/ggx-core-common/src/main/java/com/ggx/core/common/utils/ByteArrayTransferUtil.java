package com.ggx.core.common.utils;

/**
 * 字节数组转换工具类
 *
 * @author zai
 * 2020-06-10 18:57:51
 */
public class ByteArrayTransferUtil {
	
	/**
	 * int转字节数组
	 *
	 * @param n
	 * @return
	 * @author zai
	 * 2020-06-10 18:56:37
	 */
	public static byte[] intToBytes(int n) {  
		  byte[] b = new byte[4];  
		  b[3] = (byte) (n & 0xff);  
		  b[2] = (byte) (n >> 8 & 0xff);  
		  b[1] = (byte) (n >> 16 & 0xff);  
		  b[0] = (byte) (n >> 24 & 0xff);  
		  return b;  
	}
	
	/**
	 * short转字节数组
	 *
	 * @param number
	 * @return
	 * @author zai
	 * 2020-06-10 18:56:57
	 */
	public static byte[] shortToBytes(short number){ 
      int temp = number; 
      byte[] b = new byte[2]; 
      for(int i =0; i < b.length; i++){ 
          b[i]=new Integer(temp &0xff).byteValue();// 
          temp = temp >>8;// 向右移8位 
      } 
      return b; 
  }

}
