package com.ggx.files.service.model;

/**
 * 文件信息
 *
 * 2020-12-09 18:58:23
 */
public class FileInfo {
	
	
	public static final String METADATA_FILE_NAME = "filename";
	public static final String ORIGINAL_FILENAME = "originalFilename";
	public static final String CONTENT_TYPE = "contentType";
	public static final String SHA_256 = "sha256";
	
	// 文件名
	protected String filename;
	
	// 文件原名
	protected String originalFilename;
	
	// 文件类型
	protected String contentType;
	
	// sha256
	protected String sha256;
	
	// 文件大小（字节）
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
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
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

}
