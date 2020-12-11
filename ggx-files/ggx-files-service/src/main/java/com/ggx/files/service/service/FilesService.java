package com.ggx.files.service.service;

import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import com.ggx.files.service.model.FileInfo;
import com.ggx.files.service.model.ResFileInfo;
import com.ggx.util.id.GGXRandomIdUtil;
import com.ggx.util.logger.GGXLogUtil;
import com.mongodb.client.gridfs.model.GridFSFile;

/**
 * 平台缓存服务
 *
 * @author zai 2020-11-17 14:23:30
 */
@Component
public class FilesService {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	/**
	 * 存储文件
	 *
	 * @param in
	 * @param size
	 * @param originalFilename
	 * @return
	 * @throws Exception
	 * 2020-12-10 11:48:42
	 */
	public FileInfo store(InputStream in, long size, String sha256, String originalFilename) throws Exception {
		String filename = GGXRandomIdUtil.newRandomStringId24();
		String ext = getExt(originalFilename);
		
		Document metadataDocument = new Document();
		metadataDocument.put(FileInfo.ORIGINAL_FILENAME, originalFilename);
		metadataDocument.put(FileInfo.CONTENT_TYPE, ext);
		metadataDocument.put(FileInfo.SHA_256, sha256);
		this.gridFsTemplate.store(in, filename, metadataDocument);

		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(filename);
		fileInfo.setOriginalFilename(originalFilename);
		fileInfo.setSha256(sha256);
		fileInfo.setSize(size);
		return fileInfo;
	}
	
	/**
	 * 根据文件名查找
	 *
	 * @param filename
	 * @return
	 * 2020-12-10 11:48:18
	 */
	public FileInfo findByFilename(String filename) {
		GridFSFile fsFile = this.gridFsTemplate.findOne(Query.query(GridFsCriteria.whereFilename().is(filename)));
		return makeFileInfo(fsFile);
	}
	
	/**
	 * 生成fileinfo
	 *
	 * @param fsFile
	 * @return
	 * 2020-12-11 11:58:59
	 */
	private FileInfo makeFileInfo(GridFSFile fsFile) {
		if (fsFile == null) {
			return null;
		}
		Document metadata = fsFile.getMetadata();
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFilename(fsFile.getFilename());
		fileInfo.setSize(fsFile.getLength());
		
		if (metadata != null) {
			fileInfo.setOriginalFilename(metadata.getString(FileInfo.ORIGINAL_FILENAME));			
			fileInfo.setContentType(metadata.getString(FileInfo.CONTENT_TYPE));			
			fileInfo.setSha256(metadata.getString(FileInfo.SHA_256));			
		}
		
		return fileInfo;
	}
	
	/**
	 * 根据文件名获取
	 *
	 * @param filename
	 * @return
	 * 2020-12-11 11:52:05
	 */
	public ResFileInfo findResourceByFilename(String filename) {
		GridFsResource resource = this.gridFsTemplate.getResource(filename);
		GridFSFile fsFile = resource.getGridFSFile();
		if (fsFile == null) {
			return null;
		}
		Document metadata = fsFile.getMetadata();
		
		ResFileInfo fileInfo = new ResFileInfo();
		fileInfo.setFilename(filename);
		fileInfo.setSize(fsFile.getLength());
		fileInfo.setInputStream(resource.getContent());
		if (metadata != null) {
			fileInfo.setOriginalFilename(metadata.getString(FileInfo.ORIGINAL_FILENAME));			
			fileInfo.setContentType(metadata.getString(FileInfo.CONTENT_TYPE));			
			fileInfo.setSha256(metadata.getString(FileInfo.SHA_256));					
		}
		return fileInfo;
	}
	
	/**
	 * 根据metadata获取
	 *
	 * @param sha256
	 * @return
	 * 2020-12-11 11:58:47
	 */
	public FileInfo getFileInfoBySha256(String sha256) {
		GridFSFile fsFile = this.gridFsTemplate.findOne(Query.query(GridFsCriteria.whereMetaData(FileInfo.SHA_256).is(sha256)));
		return makeFileInfo(fsFile);
	}

	/**
	 * 根据文件名删除
	 *
	 * @param filename
	 * @throws Exception
	 * 2020-12-10 11:48:31
	 */
	public void deleteByFilename(String filename) throws Exception {
		this.gridFsTemplate.delete(Query.query(GridFsCriteria.whereFilename().is(filename)));
	}
	
	/**
	 * 获取拓展名
	 * 
	 * @param originalFilename
	 * @return
	 * 
	 * @author zai 2017-07-24
	 */
	private String getExt(String originalFilename) {
		try {
			if (originalFilename.contains(".")) {
				return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).warn("Get file ext name error!", e);
		}
		return "unknow";
	}
	
	public static void main(String[] args) {
		System.out.println(DigestUtils.sha256Hex("xxoo").length());
	}
}
