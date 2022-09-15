package com.dongdong.usercenter.service;

import com.dongdong.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务接口
 *
 * @author Mr.Ye
 */
public interface UserService extends IService<User> {

	/**
	 * 用户注册
	 *
	 * @param userAccount 要用户账号
	 * @param userPassword 用户密码
	 * @param checkPassword 二次输入密码
	 * @param planetCode 星球编号
	 * @return 用户唯一ID
	 */
	Long userRegister(String userAccount, String userPassword, String checkPassword, Integer planetCode);

	/**
	 * 用户登录
	 *
	 * @param userAccount 用户账号
	 * @param userPassword 用户密码
	 * @param request 请求对象
	 * @return 用户的基本信息
	 */
	User userLogin(String userAccount, String userPassword, HttpServletRequest request);

	/**
	 * 用户注销
	 *
	 * @param request 请求对象
	 * @return 成功返回值1
	 */
	Integer userLogout(HttpServletRequest request);

	/**
	 * 查找用户
	 *
	 * @param userName 用户昵称
	 * @return 用户列表
	 */
	List<User> searchUsers(String userName);

	/**
	 * 根据标签列表查找用户
	 *
	 * @param tagNameList 标签列表
	 * @return 符合标签的用户列表
	 */
	List<User> searchUsersByTags(List<String> tagNameList);

	/**
	 * 更新用户信息
	 *
	 * @param user 更新后的用户信息
	 * @param user 当前登录的用户信息
	 * @return 更新结果，1 成功，0 失败
	 */
	Integer updateUser(User user, User loginUser);

	/**
	 * 根据ID删除用户
	 *
	 * @param id 用户ID
	 * @return 删除结果
	 */
	Integer deleteById(Long id);

	/**
	 * 获取当前登录用户
	 *
	 * @param request HTTP请求对象
	 * @return 当前登录用户
	 */
	User getLoginUser(HttpServletRequest request);

	/**
	 * 判断是否是管理员
	 *
	 * @param request 请求对象
	 * @return 判断结果
	 */
	Boolean isAdmin(HttpServletRequest request);

	/**
	 * 判断是否是管理员
	 *
	 * @param loginUser 当前登录用户
	 * @return 判断结果
	 */
	Boolean isAdmin(User loginUser);

	/**
	 * 分页获取推荐用户
	 *
	 * @param pageNum 当前页
	 * @param pageSize 每页显示条数
	 * @return 当前页的用户
	 */
	List<User> recommendUsers(Long pageNum, Long pageSize, HttpServletRequest request);

	/**
	 * 用户信息脱敏
	 *
	 * @param originUser 原始用户信息
	 * @return 脱敏后的用户信息
	 */
	User getSafeUser(User originUser);
}
