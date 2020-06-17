package com.ggx.core.common.encryption.aes.impl;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.ggx.core.common.encryption.aes.AESCipher;
import com.ggx.core.common.random.CustomRandom;

public class DefaultAESCipher implements AESCipher {
	
	private static final String SEED_ARR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+=";
	
	//算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
	
	private static final ThreadLocal<Cipher> ENCRYPT_CIPHER_LOCAL = new ThreadLocal<Cipher>();
	
	private static final ThreadLocal<Cipher> DECRYPT_CIPHER_LOCAL = new ThreadLocal<Cipher>();

	private String encryptKey = "";

	private SecretKey secretKey;
	
	public DefaultAESCipher() {
		init();
		
	}

	public DefaultAESCipher(String encryptKey) {
		this.encryptKey = encryptKey;
		init();
	}

	public void init() {
		if (this.encryptKey == null || this.encryptKey.isEmpty()) {
			CustomRandom random = new CustomRandom(4 * 5 + 1 + 2 * 3);
			for (int i = 0; i < SEED_ARR.length() / 4; i++) {
				this.encryptKey += SEED_ARR.charAt(random.nextInt(SEED_ARR.length()));
			}
		}
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			this.secretKey = new SecretKeySpec(encryptKey.getBytes("utf-8"), "AES");// 转换为AES专用密钥
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public byte[] encrypt(byte[] content) {
		try {
			return getEncryptCipher().doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] decrypt(byte[] content) {
		try {
			return getDecryptCipher().doFinal(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Cipher getDecryptCipher() {
		try {
			Cipher cipher = DECRYPT_CIPHER_LOCAL.get();
			if (cipher == null) {
				cipher = Cipher.getInstance(ALGORITHMSTR);
				cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
				DECRYPT_CIPHER_LOCAL.set(cipher);
			}
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Cipher getEncryptCipher() {
		try {
			Cipher cipher = ENCRYPT_CIPHER_LOCAL.get();
			if (cipher == null) {
				cipher = Cipher.getInstance(ALGORITHMSTR);
				cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
				ENCRYPT_CIPHER_LOCAL.set(cipher);
			}
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getEncryptKey() {
		return this.encryptKey;
	}

}
