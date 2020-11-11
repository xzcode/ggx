package com.ggx.util.md5;

import org.apache.commons.codec.digest.DigestUtils;

public class GGXMd5Util {
	
	
	public static String md5Hex(String str) {
		return DigestUtils.md5Hex(str);
	}
	

}
