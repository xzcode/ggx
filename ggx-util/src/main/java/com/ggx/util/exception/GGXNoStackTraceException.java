package com.ggx.util.exception;

/**
 * 无栈跟踪运行时异常
 * 
 * @author zai
 * 2020-10-6 15:23:23
 */
public class GGXNoStackTraceException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public Throwable fillInStackTrace() {
		return null;
	}
	
}
