package com.dongdong.usercenter.common;

/**
 * @program: user-center
 * @description: 全局通用状态码
 * @author: Mr.Ye
 * @create: 2022-05-28 22:17
 **/
public enum ErrorCode{
	// 请求的参数不正确或为空
	PARAMS_ERROR(40000, "请求参数错误", ""),
	// 返回的数据为空
	NULL_ERROR(40001, "请求数据为空", ""),
	NOT_LOGIN_ERROR(40100, "用户未登录", ""),
	NOT_AUTH_ERROR(40101, "用户无权限", ""),
	TOO_FREQUENCY_ERROR(42900, "验证码发送太频繁",""),
	NOT_FOUND_ERROR(40400, "请求资源不存在", ""),
	SYSTEM_ERROR(50000, "系统错误", "");

	/**
	 * 状态码
	 */
	private final Integer code;
	/**
	 * 返回信息
	 */
	private final String message;
	/**
	 * 详细描述
	 */
	private final String description;

	ErrorCode(Integer code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDescription() {
		return description;
	}
}
