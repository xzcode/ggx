package com.ggx.files.service.controller.files.error;

import com.ggx.spring.common.base.exception.LogicException;

@SuppressWarnings("serial")
public class FileOversizeError extends LogicException{

	public static final FileOversizeError INSTANCE = new FileOversizeError();

	public FileOversizeError() {
		super("文件太大");
	}
	
}
