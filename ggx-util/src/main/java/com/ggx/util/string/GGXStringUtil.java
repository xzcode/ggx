package com.ggx.util.string;

import org.apache.commons.lang3.StringUtils;

/**
 * GGX字符串工具
 *
 * @author zai
 * 2020-11-13 11:50:01
 */
public class GGXStringUtil {
	
	
	/**
	 * 
	 * 把驼峰命名转为小写下划线分隔命名
	 *
	 * @param camelCaseString
	 * @return
	 * @author zai
	 * 2020-11-13 11:38:45
	 */
	public static String camelCaseToUnderlineLower(String camelCaseString) {
		String[] camelCaseArr = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
		for (int i = 0; i < camelCaseArr.length; i++) {
			camelCaseArr[i] = camelCaseArr[i].toLowerCase(); 
		}
		return StringUtils.join(camelCaseArr, "_");
	}
	
	/**
	 * 驼峰命名切割
	 *
	 * @param camelCaseString
	 * @param delimiter 分隔符
	 * @param caseType 大小写类型 Character.LOWERCASE_LETTER or Character.UPPERCASE_LETTER
	 * @return
	 * @author zai
	 * 2020-11-13 11:49:08
	 */
	public static String camelCaseSplit(String camelCaseString, String delimiter, Byte caseType) {
		String[] camelCaseArr = StringUtils.splitByCharacterTypeCamelCase(camelCaseString);
		if (caseType != null) {
			for (int i = 0; i < camelCaseArr.length; i++) {
				if (caseType == Character.LOWERCASE_LETTER) {
					camelCaseArr[i] = camelCaseArr[i].toLowerCase(); 
				}else if (caseType == Character.UPPERCASE_LETTER) {
					camelCaseArr[i] = camelCaseArr[i].toUpperCase(); 
				}
			}
		}
		return StringUtils.join(camelCaseArr, delimiter);
	}
	
}
