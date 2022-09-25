package com.dongdong.usercenter.interceptors;

import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.constant.UserConstant;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.utils.UserHolder;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @program: user-center
 * @description: 用户登录的拦截器
 * @author: Mr.Ye
 * @create: 2022-09-18 19:36
 **/
public class LoginInterceptor implements HandlerInterceptor {
	private StringRedisTemplate stringRedisTemplate;

	public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//判空
		if (request == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		// 预检放行，OPTIONS请求放行
		if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
			return true;
		}
		// 1、请求头获取token
		String token = request.getHeader(UserConstant.TOKEN_HEADER);
		System.out.println("token: " + token);
		// 2、从Redis中获取用户信息
		String userKey = UserConstant.LOGIN_KEY + token;
		String userJson = stringRedisTemplate.opsForValue().get(userKey);
		System.out.println("User:" + userJson);
		// 3、用户不存在，拦截
		if (StringUtils.isBlank(userJson)) {
			throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录，请重新登录！");
		}
		// 4、用户存在，将用户信息存入ThreadLocal
		User loginUser = new Gson().fromJson(userJson, User.class);
		UserHolder.saveUser(loginUser);
		// 5、刷新缓存时间：session会自动刷新缓存时间，redis需要手动刷新，此处演示如何在自己维护的类中注入对象
		stringRedisTemplate.expire(userKey, UserConstant.LOGIN_KEY_TTL, TimeUnit.MINUTES);
		// 6、放行
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 请求结束在线程域中移除用户
		UserHolder.removeUser();
	}
}
