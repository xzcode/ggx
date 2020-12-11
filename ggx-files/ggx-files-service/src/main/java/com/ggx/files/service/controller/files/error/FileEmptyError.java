package com.ggx.files.service.controller.files.error;

import com.ggx.spring.common.base.exception.LogicException;

@SuppressWarnings("serial")
public class FileEmptyError extends LogicException{

	public static final FileEmptyError INSTANCE = new FileEmptyError();

	public FileEmptyError() {
		super("空文件错误");
	}
	
}
