package com.ggx.files.service.controller.files.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("文件上传响应")
public class UploadFileResp {
	
	@ApiModelProperty("文件名")
	protected String filename;
	
	@ApiModelProperty("文件原名")
	protected String originalFilename;
	
	@ApiModelProperty("文件类型")
	protected String contentType;
	
	@ApiModelProperty("sha256")
	protected String sha256;
	
	@ApiModelProperty("文件大小")
	protected long size;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	
	

}
