package com.ggx.files.service.controller.files.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("文件信息响应")
public class FileInfoResp {
	
	@ApiModelProperty("文件名")
	protected String filename;
	
	@ApiModelProperty("文件类型")
	protected String contentType;
	
	@ApiModelProperty("文件大小")
	protected long size;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	
	

}
