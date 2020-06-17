package com.ggx.core.common.encryption.aes;

/**
 * AES加密接口
 *
 * @author zai
 * 2020-06-10 10:38:18
 */
public interface AESCipher {
	
	/**
	 * 加密
	 *
	 * @param content
	 * @return
	 * @author zai 2020-06-10 10:57:55
	 */
	byte[] encrypt(byte[] content);
	
	
	/**
	 * 解密
	 *
	 * @param content
	 * @return
	 * @author zai 2020-06-10 10:58:07
	 */
	byte[] decrypt(byte[] content);
	
	/**
	 * 获取加密秘钥
	 *
	 * @return
	 * @author zai
	 * 2020-06-10 11:45:14
	 */
	String getEncryptKey();


}
