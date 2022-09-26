package com.dongdong.usercenter.utils;

import com.dongdong.usercenter.model.domain.User;

/**
 * @program: user-center
 * @description: 用户的ThreadLocal类
 * @author: Mr.Ye
 * @create: 2022-09-18 19:31
 **/
public class UserHolder {
	private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();

	private static String userKey;

	public static void setUserKey(String userKey) {
		UserHolder.userKey = userKey;
	}

	public static String getUserKey() {
		return userKey;
	}
	
	public static void saveUser(User user) {
		THREAD_LOCAL.set(user);
	}
	
	public static User getUser() {
		return THREAD_LOCAL.get();
	}
	
	public static void removeUser() {
		THREAD_LOCAL.remove();
	}
	

}
