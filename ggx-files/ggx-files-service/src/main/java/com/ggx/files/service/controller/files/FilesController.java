package com.ggx.files.service.controller.files;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ggx.files.service.controller.files.error.FileNotFoundError;
import com.ggx.files.service.controller.files.model.FileInfoResp;
import com.ggx.files.service.controller.files.model.UploadFileResp;
import com.ggx.files.service.model.FileInfo;
import com.ggx.files.service.model.ResFileInfo;
import com.ggx.files.service.service.FilesService;
import com.ggx.spring.common.base.util.RestResponse;
import com.ggx.util.logger.GGXLogUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/files")
@Api(tags = "文件模块")
public class FilesController  {

	@Autowired
	private FilesService filesService;

	@ApiOperation(value = "上传文件")
	@PostMapping(value = "/upload/file", headers = "content-type=multipart/form-data")
	public RestResponse<UploadFileResp> upload(HttpServletRequest request,
		@RequestParam("uploadFile") MultipartFile uploadFile) throws Exception {
		InputStream inputStream = uploadFile.getInputStream();
		String sha256Hex = DigestUtils.sha256Hex(inputStream);
		inputStream.close();
		//校验是否已上传过相同图片
		FileInfo fileInfo = this.filesService.getFileInfoBySha256(sha256Hex);
		if (fileInfo == null) {
			//保存未上传过的图片
			fileInfo = this.filesService.store(uploadFile.getInputStream(), uploadFile.getSize(), sha256Hex, uploadFile.getOriginalFilename());
		}
		UploadFileResp resp = new UploadFileResp();
		BeanUtils.copyProperties(fileInfo, resp);
		return RestResponse.success(resp);
	}
	
	@ApiOperation(value = "秒传")
	@GetMapping("/quick/upload/{sha256}")
	public RestResponse<FileInfoResp> upload(HttpServletRequest request,
		@PathVariable String sha256) throws Exception {
		FileInfoResp resp = null;
		if (sha256.length() < 64) {
			return RestResponse.fail(resp).setMessage("Not a sha256 string!");
		}
		FileInfo fileInfo = this.filesService.getFileInfoBySha256(sha256);
		if (fileInfo != null) {
			resp = new FileInfoResp();
			BeanUtils.copyProperties(fileInfo, resp);
			return RestResponse.success(resp);
		}
		return RestResponse.fail(resp);
	}

	@ApiOperation("获取文件")
	@GetMapping(value = "/fetch/{filename}")
	public void get(HttpServletRequest request, HttpServletResponse response,
		@PathVariable("filename") String filename) throws Exception {
		ResFileInfo res = this.filesService.findResourceByFilename(filename);
		if (res == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			throw FileNotFoundError.INSTANCE;
		}
		try (InputStream in = res.getInputStream(); ServletOutputStream out = response.getOutputStream();) {
			if (res != null) {
				IOUtils.copy(in, out, 8192);
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Get file error!", e);
		}
	}

	@ApiOperation("下载文件")
	@GetMapping(value = "/download/{filename}")
	public void download(HttpServletRequest request, HttpServletResponse response,
		@PathVariable String filename) throws Exception {

		ResFileInfo res = this.filesService.findResourceByFilename(filename);
		try (InputStream in = res.getInputStream(); ServletOutputStream out = response.getOutputStream();) {
			if (res != null) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition", "attachment; filename=" + res.getOriginalFilename());
				IOUtils.copy(in, out, 8192);
			}
		} catch (Exception e) {
			GGXLogUtil.getLogger(this).error("Download file error!", e);
		}
	}
	
	
}
