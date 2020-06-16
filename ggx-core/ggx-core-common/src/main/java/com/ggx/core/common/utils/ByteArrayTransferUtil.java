package com.ggx.core.common.utils;

import java.util.Arrays;

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
		  return new byte[] {(byte) (n & 0xff), (byte) (n >> 8 & 0xff), (byte) (n >> 16 & 0xff), (byte) (n >> 24 & 0xff)};  
	}
	
	/**
	 * 把int值转换成字节并填充到指定缓冲数组
	 *
	 * @param n
	 * @param start
	 * @param dist
	 * @return
	 * @author zai
	 * 2020-06-16 11:28:27
	 */
	public static void intToBytesAndFillArray(int n, int start, byte[] dist) {  
		dist[start] 	= (byte) (n & 0xff);
		dist[start + 1] = (byte) (n >> 8 & 0xff);
		dist[start + 2] = (byte) (n >> 16 & 0xff);
		dist[start + 3] = (byte) (n >> 24 & 0xff);
	}
	
	/**
	 * 字节数组转int值
	 *
	 * @param bytes
	 * @return
	 * @author zai
	 * 2020-06-15 14:23:21
	 */
	public static int bytesToInt(byte[] bytes ) {
		return bytes[0] & 0xff | ((bytes[1] & 0xff) << 8) | ((bytes[2] & 0xff) << 16) | ((bytes[3] & 0xff) << 24);
	}
	
	/**
	 * 字节数组转int
	 *
	 * @param bytes
	 * @param startOffset
	 * @return
	 * @author zai
	 * 2020-06-16 11:00:09
	 */
	public static int bytesToInt(byte[] bytes, int startOffset) {
		return bytes[startOffset] & 0xff | ((bytes[startOffset + 1] & 0xff) << 8) | ((bytes[startOffset + 2] & 0xff) << 16) | ((bytes[startOffset + 3] & 0xff) << 24);
	}
	
	/**
	 * 字节数组转无符号short
	 *
	 * @param bytes
	 * @return
	 * @author zai
	 * 2020-06-15 14:36:19
	 */
	public static short bytesToUnsignedShort(byte[] bytes) {  
		return (short) (((bytes[1] << 8) | bytes[0] & 0xff));  
	}
	
	/**
	 * 字节数组转无符号short
	 *
	 * @param bytes 字节数组
	 * @param startOffset 字节数组中的起始位置
	 * @return
	 * @author zai
	 * 2020-06-15 15:10:39
	 */
	public static short bytesToUnsignedShort(byte[] bytes, int startOffset) {  
        return (short) (((bytes[startOffset + 1] << 8) | bytes[startOffset] & 0xff));  
    }
	
	/**
	 * 无符号short转字节数组
	 *
	 * @param number
	 * @return
	 * @author zai
	 * 2020-06-10 18:56:57
	 */
	public static byte[] unsignedShortToBytes(short number){ 
		return new byte[] {(byte) (number >> 8 & 0xFF), (byte) (number & 0xFF)}; 
    }
	
	
	/**
	 * 把short值转换成字节并填充到指定缓冲数组
	 *
	 * @param n
	 * @param start
	 * @param dist
	 * @return
	 * @author zai
	 * 2020-06-16 11:28:27
	 */
	public static void unsignedShortToBytesAndFillArray(int n, int start, byte[] dist) {  
		dist[start] 	= (byte) (n >> 8 & 0xff);
		dist[start + 1] = (byte) (n & 0xff);
	}
	
	/**
	 * 读取字节
	 *
	 * @param original
	 * @param from
	 * @param to
	 * @return
	 * @author zai
	 * 2020-06-16 11:13:13
	 */
	public static byte[] readBytes(byte[] original, int from, int to) {
		return Arrays.copyOfRange(original, from, to);
	}
	
	/**
	 * 把字节数组填充到指定的数组内
	 *
	 * @param original
	 * @param start
	 * @param dist
	 * @return
	 * @author zai
	 * 2020-06-16 11:35:07
	 */
	public static void fillBytes(byte[] original, int start, byte[] dist) {
		for (int i = start, j = 0; i < original.length; i++, j++) {
			dist[i] = original[j];
		}
	}
	

}
