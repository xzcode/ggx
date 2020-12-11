package com.ggx.files.service.controller.files.error;

import com.ggx.spring.common.base.exception.LogicException;

@SuppressWarnings("serial")
public class FileNotFoundError extends LogicException{

	public static final FileNotFoundError INSTANCE = new FileNotFoundError();

	public FileNotFoundError() {
		super("找不到该文件");
	}
	
}
