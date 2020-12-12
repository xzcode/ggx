package com.ggx.util.id;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机id工具
 *
 * @author zai
 * 2020-04-09 11:58:25
 */
public class GGXRandomIdUtil {
	
	private static final String RAND_STRINGS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String newUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String newRandomStringId32() {
		return newRandomStringId(32);
	}
	
	
	public static String newRandomStringId24() {
		return newRandomStringId(24);
	}
	
	public static String newRandomStringId16() {
		return newRandomStringId(16);
	}
	
	
	public static String newRandomStringId6() {
		return newRandomStringId(6);
	}
	
	public static String newRandomStringId(int len) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(RAND_STRINGS.charAt(random.nextInt(RAND_STRINGS.length())));
		}
		return sb.toString();
	}
	
	
}
