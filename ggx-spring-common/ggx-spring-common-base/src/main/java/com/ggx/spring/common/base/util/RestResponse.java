package com.ggx.spring.common.base.util;

public class RestResponse<T> {
	
	private boolean success;
	
	private String code = "-1";

	private String message;

	private T data;
	
	public RestResponse(boolean success){
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public RestResponse<T>  setCode(String code) {
		this.code = code;
		return this;
	}

	public T getData() {
		return data;
	}

	public RestResponse<T> setData(T data) {
		this.data = data;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public RestResponse<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public static RestResponse<?> success() {
		return new RestResponse<>(true).setCode("1");
	}
	
	public static RestResponse<?> success(String successMessage) {
		return new RestResponse<>(true).setCode("1").setMessage(successMessage);
	}
	public static <T> RestResponse<T> success(T data) {
		return new RestResponse<T>(true).setCode("1").setData(data);
	}

	public static RestResponse<?> fail() {
		return new RestResponse<>(false).setCode("-1");
	}
	
	public static <T> RestResponse<T> fail(T data) {
		return new RestResponse<T>(false).setData(data);
	}
	public static RestResponse<?> fail(String code) {
		return new RestResponse<>(false).setCode(code);
	}
	public static RestResponse<?> fail(String code, String message) {
		return new RestResponse<>(false).setCode(code).setMessage(message);
	}
	
	

	public RestResponse(boolean success, String code, String message, T data) {
		super();
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public RestResponse(String code, String message, T data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public RestResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public RestResponse() {
		super();
	}
	
	
	
}
