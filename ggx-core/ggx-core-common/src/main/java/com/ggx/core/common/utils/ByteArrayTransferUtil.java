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
	 * 字节数组转int值
	 *
	 * @param bytes
	 * @return
	 * @author zai
	 * 2020-06-15 14:23:21
	 */
	public static int bytesToInt(byte[] bytes ) {
		
		int int1 = bytes[0] & 0xff;
		int int2 = (bytes[1] & 0xff) << 8;
		int int3 = (bytes[2] & 0xff) << 16;
		int int4 = (bytes[3] & 0xff) << 24;
	    
		return int1 | int2 | int3 | int4;
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
		byte[] temp = new byte[2]; 
		temp[0] = (byte) (number >> 8 & 0xFF); 
		temp[1] = (byte) (number & 0xFF); 
		return temp; 
    }
	

}
