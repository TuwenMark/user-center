package com.dongdong.usercenter.utils;

import com.dongdong.usercenter.common.BaseResponse;
import com.dongdong.usercenter.common.ErrorCode;

/**
 * @program: user-center
 * @description: 全局通用返回对象工具类
 * @author: Mr.Ye
 * @create: 2022-05-28 21:48
 **/
public class ResponseUtils {
	/**
	 * 获得通用成功响应对象
	 *
	 * @param data	返回数据
	 * @param <T>	泛型
	 * @return	成功响应对象
	 */
	public static <T> BaseResponse<T> success(T data) {
		return new BaseResponse(20000, data, "OK", "");
	}

	public static BaseResponse error(ErrorCode errorCode) {
		return new BaseResponse(errorCode);
	}

	public static BaseResponse error(ErrorCode errorCode, String description) {
		return new BaseResponse(errorCode, description);
	}

	public static BaseResponse error(Integer code, String message, String description) {
		return new BaseResponse(code, message, description);
	}


}
