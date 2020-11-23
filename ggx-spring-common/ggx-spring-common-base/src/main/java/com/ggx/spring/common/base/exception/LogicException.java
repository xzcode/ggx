package com.ggx.spring.common.base.exception;

import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

/**
 * 逻辑异常
 *
 * @author zai
 * 2020-09-29 14:04:08
 */
@SuppressWarnings("serial")
public class LogicException extends GGXNoStackTraceRuntimeException {
	
    /**
     * 错误码
     */
	private String code;
	
	/**
	 * 错误消息
	 */
	private String msg;
	
	public LogicException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public LogicException(String msg) {
		this.code = makeLogicCode();
		this.msg = msg;
	}
	
	public String makeLogicCode() {
		Class<?> clazz = getClass();
		String simpleName = clazz.getSimpleName();
		simpleName = simpleName.replace("Error", "");
		simpleName = simpleName.replace("Exception", "");
		int len = simpleName.length();
		StringBuilder sb = new StringBuilder(32);
		int readIndex = 0;
		for (int i = 0; i < len; i++) {
			char ca = simpleName.charAt(i);
			if (Character.isUpperCase(ca)) {
				if (i == 0) {
					continue;
				}
				sb.append(simpleName.substring(readIndex, i).toLowerCase()).append("_");
				readIndex += i - readIndex;
			}
		}
		sb.append(simpleName.substring(readIndex, len).toLowerCase());
		return sb.toString().toUpperCase();
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg(){
		return msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
	@Override
	public Throwable fillInStackTrace() {
		return null;
	}

	
}
