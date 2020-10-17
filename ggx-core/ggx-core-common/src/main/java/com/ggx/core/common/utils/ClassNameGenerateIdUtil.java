package com.ggx.core.common.utils;

public class ClassNameGenerateIdUtil {
	
	public static String generateClassNameDotId(Class<?> clazz) {
		return generateClassNameId(clazz, ".");
	}
	public static String generateClassNameSublineSplitId(Class<?> clazz) {
		return generateClassNameId(clazz, "_");
	}
	public static String generateClassNameId(Class<?> clazz, String delimiter) {
		String simpleName = clazz.getSimpleName();
		int len = simpleName.length();
		StringBuilder sb = new StringBuilder(32);
		int readIndex = 0;
		for (int i = 0; i < len; i++) {
			char ca = simpleName.charAt(i);
			if (Character.isUpperCase(ca)) {
				if (i == 0) {
					continue;
				}
				sb.append(simpleName.substring(readIndex, i).toLowerCase()).append(delimiter);
				readIndex += i - readIndex;
			}
		}
		sb.append(simpleName.substring(readIndex, len).toLowerCase());
		return sb.toString();
	}

}
