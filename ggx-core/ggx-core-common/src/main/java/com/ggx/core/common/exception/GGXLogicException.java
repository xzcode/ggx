package com.ggx.core.common.exception;

/**
 * GGX逻辑异常
 *
 * @author zai
 * 2020-09-29 15:57:56
 */
public class GGXLogicException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}
}
