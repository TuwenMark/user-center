package com.dongdong.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongdong.usercenter.common.BaseResponse;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.model.DTO.UserLoginRequest;
import com.dongdong.usercenter.model.DTO.UserRegisterRequest;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.service.UserService;
import com.dongdong.usercenter.utils.ResponseUtils;
import com.dongdong.usercenter.utils.UserHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @program: user-center
 * @description: 用户服务接口
 * @author: Mr.Ye
 * @create: 2022-05-14 17:43
 **/
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class UserController {

	@Resource
	private UserService userService;

	/**
	 * 用户注册接口
	 *
	 * @param userRegisterRequest 用户注册请求体参数
	 * @return 用户ID
	 */
	@PostMapping("/register")
	public BaseResponse<Long> userRegister(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
		if (userRegisterRequest == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "请求对象为空");
		}
		// 简单的校验，不涉及业务逻辑
		String userAccount = userRegisterRequest.getUserAccount();
		String userPassword = userRegisterRequest.getUserPassword();
		String checkPassword = userRegisterRequest.getCheckPassword();
		Integer planetCode = userRegisterRequest.getPlanetCode();
		if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode + "")) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
		}
		Long id = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		return ResponseUtils.success(id);
	}

	/**
	 * 用户登录接口
	 *
	 * @param userLoginRequest   用户登录请求体参数
	 * @param httpServletRequest 请求对象
	 * @return 用户基本信息
	 */
	@PostMapping("/login")
	public BaseResponse<String> userLoginByPassword(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
		if (userLoginRequest == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码为空");
		}
		// 简单的校验，不涉及业务逻辑
		String userAccount = userLoginRequest.getUserAccount();
		String userPassword = userLoginRequest.getUserPassword();
		if (StringUtils.isAnyBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码为空");
		}
		String token = userService.userLoginByPassword(userAccount, userPassword, httpServletRequest);
		return ResponseUtils.success(token);
	}

	/**
	 * 发送验证码
	 *
	 * @param userLoginRequest 用户登录请求
	 * @return 发送结果
	 */
	@PostMapping("/code")
	public BaseResponse sendCode(@RequestBody UserLoginRequest userLoginRequest) {
		// 判空
		if (userLoginRequest == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "请求为空");
		}
		userService.sendCode(userLoginRequest);
		return ResponseUtils.success();
	}

	/**
	 * 用户手机号登录接口
	 *
	 * @param userLoginRequest   用户登录请求体参数
	 * @param httpServletRequest 请求对象
	 * @return 用户基本信息
	 */
	@PostMapping("/login/phone")
	public BaseResponse<String> userLoginByCode(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
		if (userLoginRequest == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "请求对象为空");
		}
		String token = userService.userLoginByCode(userLoginRequest, httpServletRequest);
		return ResponseUtils.success(token);
	}

	/**
	 * 用户注销接口
	 *
	 * @param httpServletRequest 请求对象
	 * @return 成功返回值
	 */
	@GetMapping("/logout")
	public BaseResponse<Integer> userLogin(HttpServletRequest httpServletRequest) {
		if (httpServletRequest == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "请求对象为空");
		}
		Integer result = userService.userLogout(httpServletRequest);
		return ResponseUtils.success(result);
	}

	/**
	 * 获取当前用户
	 *
	 * @param request 请求对象
	 * @return 脱敏的当前用户信息
	 */
	@GetMapping("/current")
	public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
		// 获取当前用户信息的ID
//		User originUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
		User originUser = UserHolder.getUser();
		if (originUser == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
		}
		Long id = originUser.getId();
		// 利用ID查询数据库，获取最新的用户信息返回
		User user = userService.getById(id);
		User safeUser = userService.getSafeUser(user);
		return ResponseUtils.success(safeUser);
	}

	/**
	 * 查找所有用户
	 *
	 * @param request 请求对象
	 * @return 符合条件的用户列表
	 */
	@GetMapping("/search")
	public BaseResponse<List<User>> searchUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
		// 校验是否是管理员
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "用户非管理员");
		}
		QueryWrapper queryWrapper = new QueryWrapper();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.like("username", username);
		}
		List<User> users = userService.list(queryWrapper);
		List<User> safeUsers = users.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
		return ResponseUtils.success(safeUsers);
	}

	/**
	 * 推荐所有用户
	 *
	 * @return 所有的用户列表
	 */
	@GetMapping("/recommend")
	public BaseResponse<List<User>> recommendUsers(Long pageNum, Long pageSize, HttpServletRequest request) {
		List<User> users = userService.recommendUsers(pageNum, pageSize, request);
		return ResponseUtils.success(users);
	}

	/**
	 * 根据标签查找用户
	 *
	 * @param tagNameList 标签列表
	 * @return 符合标签的用户列表
	 */
	@GetMapping("/search/tags")
	public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
		if (CollectionUtils.isEmpty(tagNameList)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		List<User> userList = userService.searchUsersByTags(tagNameList);
		return ResponseUtils.success(userList);
	}

	@PostMapping("/update")
	public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
		// 校验当前参数是否为空
		if (user == null || StringUtils.isAllBlank(user.getUsername(), user.getAvatarUrl(), user.getPhone(), user.getEmail(), String.valueOf(user.getGender()))) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 1. 获取当前登录用户
		User currentUser = UserHolder.getUser();
		// 2. 校验权限
		// 3. 操作修改
		Integer result = userService.updateUser(user, currentUser);
		return ResponseUtils.success(result);
	}

	/**
	 * 根据ID删除用户
	 *
	 * @param id      用户ID
	 * @param request 请求对象
	 * @return 删除的用户数量
	 */
	@DeleteMapping("/delete")
	public BaseResponse<Integer> deleteUser(@RequestParam Long id, HttpServletRequest request) {
		if (request == null || id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "用户非管理员");
		}
		Integer result = userService.deleteById(id);
		return ResponseUtils.success(result);
	}

}
