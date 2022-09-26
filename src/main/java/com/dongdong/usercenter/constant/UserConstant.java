package com.dongdong.usercenter.constant;

/**
 * @program: user-center
 * @description: 用户相关常量
 * @author: Mr.Ye
 * @create: 2022-05-15 12:42
 **/
public interface UserConstant {
	/**
	 * 用户的登录状态
	 */
	String USER_LOGIN_STATE = "userLoginState";

	/**
	 * 默认角色
	 */
	Integer DEFAULT_ROLE = 0;

	/**
	 * 管理员角色
	 */
	Integer ADMIN_ROLE = 1;

	/**
	 * 推荐功能的Redis缓存key
	 */
	String RECOMMEND_KEY = "dongdong:user:recommend:";

	/**
	 * 推荐功能Redis缓存key过期时间，分钟
	 */
	Long RECOMMEND_KEY_TTL = 1800L;

	/**
	 * 验证码Redis缓存key
	 */
	String CODE_KEY = "dongdong:user:phone:";

	/**
	 * 验证码Redis缓存key过期时间，秒
	 */
	Long CODE_KEY_TTL = 300L;

	/**
	 * 用户登录存入的用户信息key
	 */
	String LOGIN_KEY = "dongdong:user:login:";

	/**
	 * 用户登录信息缓存过期时间 分钟
	 */
	Long LOGIN_KEY_TTL = 30L;

	/**
	 * 缓存预热的分布式锁key
	 */
	String PRECACHE_LOCK_KEY = "dongdong:user:precache:lock";

	/**
	 * Cookie中token请求头
	 */
	String TOKEN_HEADER = "Authorization";

	/**
	 * 账户不校验，不包含特殊字符
	 */
	String ACCOUNT_REGEX = "\\pP|\\pS|\\s+";

	/**
	 * 手机号校验正则
	 */
	String PHONE_REGEX = "^([1][3,4,5,6,7,8,9])\\d{9}$";

	/**
	 * 随机生成的用户名前缀
	 */
	String USERNAME_PREFIX = "user_";
}
