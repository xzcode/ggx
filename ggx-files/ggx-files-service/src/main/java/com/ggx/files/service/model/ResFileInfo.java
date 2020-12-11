package com.ggx.files.service.model;

import java.io.InputStream;

/**
 * 文件信息
 *
 * 2020-12-09 18:58:23
 */
public class ResFileInfo extends FileInfo{
	
	
	protected InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
