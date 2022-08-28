package com.dongdong.usercenter.exception;

import com.dongdong.usercenter.common.ErrorCode;

/**
 * @program: user-center
 * @description: 业务自定义异常类
 * @author: Mr.Ye
 * @create: 2022-05-28 22:46
 **/
public class BusinessException extends RuntimeException{

	private final Integer code;

	private final String description;

	public BusinessException(String message, Integer code, String description) {
		super(message);
		this.code = code;
		this.description = description;
	}

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
		this.description = errorCode.getDescription();
	}

	public BusinessException(ErrorCode errorCode, String description) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
