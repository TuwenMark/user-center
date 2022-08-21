package com.dongdong.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: user-center
 * @description: 全局通用返回类
 * @author: Mr.Ye
 * @create: 2022-05-28 21:45
 **/
@Data
public class BaseResponse<T> implements Serializable {
	private static final long serialVersionUID = 629438373910707356L;

	private Integer code;

	private T data;

	private String message;

	private String description;

	/**
	 * base response
	 *
	 * @param code	返回码
	 * @param data	返回数据
	 * @param message	返回信息
	 */
	public BaseResponse(Integer code, T data, String message, String description) {
		this.code = code;
		this.data = data;
		this.message = message;
		this.description = description;
	}

	/**
	 * success response
	 *
	 * @param data	返回数据
	 * @param message	返回信息
	 */
	public BaseResponse(T data, String message) {
		this.code = 20000;
		this.data = data;
		this.message = message;
		this.description = "";
	}

	/**
	 * fail response
	 *
	 * @param code	状态码
	 * @param message	返回信息
	 * @param description	详细描述
	 */
	public BaseResponse(Integer code, String message, String description) {
		this.code = code;
		this.data = null;
		this.message = message;
		this.description = description;
	}

	/**
	 * fail response
	 *
	 * @param errorCode 错误码
	 */
	public BaseResponse(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.data = null;
		this.message = errorCode.getMessage();
		this.description = errorCode.getDescription();
	}

	/**
	 * fail response
	 *
	 * @param errorCode 错误码
	 * @param description	详细描述
	 */
	public BaseResponse(ErrorCode errorCode, String description) {
		this.code = errorCode.getCode();
		this.data = null;
		this.message = errorCode.getMessage();
		this.description = description;
	}

	/**
	 * success response
	 *
	 * @param data	返回数据
	 */
	public BaseResponse(T data) {
		this.code = 20000;
		this.data = data;
		this.message = "OK";
		this.description = "";
	}

}
