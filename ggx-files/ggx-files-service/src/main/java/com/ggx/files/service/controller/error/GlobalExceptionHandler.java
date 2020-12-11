package com.ggx.files.service.controller.error;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ggx.spring.common.base.exception.LogicException;
import com.ggx.spring.common.base.util.RestResponse;
import com.ggx.util.exception.GGXNoStackTraceRuntimeException;

import io.swagger.annotations.Api;

@RestController
@ControllerAdvice
@Api(tags = "全局异常处理控制器")
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(LogicException.class)
	public RestResponse<?> globalException(HttpServletResponse response, LogicException ex) {
		return RestResponse.fail(ex.getCode()).setMessage(ex.getMessage());
	}
	
	@ResponseBody
	@ExceptionHandler(GGXNoStackTraceRuntimeException.class)
	public RestResponse<?> globalException(HttpServletResponse response, GGXNoStackTraceRuntimeException ex) {
		return RestResponse.fail(ServerError500Error.INSTANCE.getCode()).setMessage(ServerError500Error.INSTANCE.getMessage());
	}

}
