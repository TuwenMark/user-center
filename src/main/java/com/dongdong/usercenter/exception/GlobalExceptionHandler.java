package com.dongdong.usercenter.exception;

import com.dongdong.usercenter.common.BaseResponse;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: user-center
 * @description: 全局异常处理器
 * @author: Mr.Ye
 * @create: 2022-05-29 00:11
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 业务异常处理
	 *
	 * @param e 业务异常
	 * @return	异常响应信息
	 */
	@ExceptionHandler(BusinessException.class)
	public BaseResponse businessExceptionHandler(BusinessException e) {
		log.error("BusinessException:" + e.getMessage(), e);
		return ResponseUtils.error(e.getCode(), e.getMessage(), e.getDescription());
	}

	/**
	 * 系统异常处理
	 *
	 * @param e	系统异常
	 * @return	异常响应信息
	 */
	@ExceptionHandler(RuntimeException.class)
	public BaseResponse RuntimeExceptionHandler(RuntimeException e) {
		log.error(e.getClass() + ":" + e.getMessage(), e);
		return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, "操作错误");
	}

}
