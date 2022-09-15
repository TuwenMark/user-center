package com.dongdong.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.constant.UserConstant;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.mapper.UserMapper;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户服务的实现类
 *
 * @author Mr.Ye
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	/**
	 * 盐值，混淆密码
	 */
	private static final String SALT = "yupi";

	@Resource
	UserMapper userMapper;

	@Resource
	StringRedisTemplate stringRedisTemplate;

	/**
	 * 用户注册
	 *
	 * @param userAccount   用户账号
	 * @param userPassword  用户密码
	 * @param checkPassword 二次输入密码
	 * @return 用户ID
	 */
	@Override
	public Long userRegister(String userAccount, String userPassword, String checkPassword, Integer planetCode) {
		// 1. 校验账号
		// 非空
		if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode + "")) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
		}
		// 账户不小于4位
		if (userAccount.length() < 4) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名称小于4位");
		}
		// 账户不包含特殊字符
		String invalidPattern = "\\pP|\\pS|\\s+";
		Matcher matcher = Pattern.compile(invalidPattern).matcher(userAccount);
		if (matcher.find()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名称包含特殊字符");
		}
		// 密码不小于8位
		if (userPassword.length() < 8 || checkPassword.length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度小于8位");
		}
		// 星球编号大于1且小于7位
		if (planetCode < 1 || (planetCode + "").length() > 7) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不符合规范");
		}
		// 密码和校验密码相同
		if (!userPassword.equals(checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
		}
		// 账户不能重复
		QueryWrapper accountQueryWrapper = new QueryWrapper();
		accountQueryWrapper.eq("user_account", userAccount);
		long count = userMapper.selectCount(accountQueryWrapper);
		if (count > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能重复");
		}
		// 星球编号不能重复
		QueryWrapper planetQueryWrapper = new QueryWrapper();
		planetQueryWrapper.eq("planet_code", planetCode);
		count = userMapper.selectCount(planetQueryWrapper);
		if (count > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不能重复");
		}

		// 2.对密码进行加密
		String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

		// 3. 插入数据
		User user = new User();
		user.setUserAccount(userAccount);
		user.setUserPassword(encryptPassword);
		user.setPlanetCode(planetCode);
		int result = userMapper.insert(user);
		if (result <= 0) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户数据插入失败");
		}
		return user.getId();
	}

	/**
	 * 用户登录
	 *
	 * @param userAccount  用户账号
	 * @param userPassword 用户密码
	 * @param request      请求对象
	 * @return 基本的用户信息
	 */
	@Override
	public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
		// 1. 校验账号
		// 非空
		if (StringUtils.isAnyBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户或密码为空");
		}
		// 账户不小于4位
		if (userAccount.length() < 4) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名称小于4位");
		}
		// 账户不包含特殊字符
		String invalidPattern = "\\pP|\\pS|\\s+";
		Matcher matcher = Pattern.compile(invalidPattern).matcher(userAccount);
		if (matcher.find()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名称包含特殊字符");
		}
		// 密码不小于8位
		if (userPassword.length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");
		}

		// 2.对密码进行加密
		String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
		// 查询数据库
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("user_account", userAccount);
		queryWrapper.eq("user_password", encryptPassword);
		User user = userMapper.selectOne(queryWrapper);
		if (user == null) {
			log.info("user login failed, userAccount and userPassword don't match.");
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
		}

		// 3. 用户信息脱敏
		User safeUser = getSafeUser(user);

		// 4. 记录用户的登录态Session
		HttpSession session = request.getSession();
		session.setAttribute(UserConstant.USER_LOGIN_STATE, safeUser);

		// 5. 返回基本的用户信息
		return safeUser;
	}

	/**
	 * 用户注销
	 *
	 * @param request 请求对象
	 * @return 成功返回值
	 */
	@Override
	public Integer userLogout(HttpServletRequest request) {
		request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
		return 20000;
	}

	/**
	 * 查找所有用户
	 *
	 * @param username 用户昵称
	 * @return 用户列表
	 */
	@Override
	public List<User> searchUsers(String username) {
		QueryWrapper queryWrapper = new QueryWrapper();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.like("username", username);
		}
		List<User> userList = userMapper.selectList(queryWrapper);
		return userList.stream().map(this::getSafeUser).collect(Collectors.toList());
	}

	/**
	 * 根据标签列表查询用户列表(通过内存查询)
	 *
	 * @param tagNameList 标签列表
	 * @return 符合标签的用户列表
	 */
	@Override
	public List<User> searchUsersByTags(List<String> tagNameList) {
		if (CollectionUtils.isEmpty(tagNameList)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入需要搜索的标签");
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		List<User> userList = userMapper.selectList(queryWrapper);
		Gson gson = new Gson();
		return userList.stream().filter(user -> {
			String tags = user.getTags();
			Set<String> tagNameSet = gson.fromJson(tags, new TypeToken<Set<String>>() {
			}.getType());
			tagNameSet = Optional.ofNullable(tagNameSet).orElse(Collections.EMPTY_SET);
//			for (String tagName : tagNameList) {
//				if(! tagNameSet.contains(tagName)) {
//					return false;
//				}
//			}
			if (!tagNameSet.containsAll(tagNameList)) {
				return false;
			}
			return true;
		}).map(this::getSafeUser).collect(Collectors.toList());
	}

	/**
	 * 修改用户信息
	 *
	 * @param user 当前登录的用户信息
	 * @param loginUser 当前登录用户
	 * @return 修改结果
	 */
	@Override
	public Integer updateUser(User user, User loginUser) {
		// 判空
		Long userId = user.getId();
		Long loginUserId = loginUser.getId();
		if (userId == null || loginUserId == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 校验权限，管理员允许更新任意用户，非管理员只能修改自己
		if (!isAdmin(loginUser) && !userId.equals(loginUserId)) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
		}
		// 操作修改
		if(userMapper.selectById(userId) ==null) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		return userMapper.updateById(user);
	}

	/**
	 * 根据用户ID删除单个用户
	 *
	 * @param id 用户ID
	 * @return 删除结果
	 */
	@Override
	public Integer deleteById(Long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID小于0");
		}
		return userMapper.deleteById(id);
	}

	/**
	 * 根据标签列表查询用户列表(通过SQL查询)
	 *
	 * @param tagNameList 标签列表
	 * @return 符合标签条件的用户
	 */
	@Deprecated
	public List<User> searchUsersByTagsThroughSQL(List<String> tagNameList) {
		if (CollectionUtils.isEmpty(tagNameList)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入需要搜索的标签");
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		for (String tagName : tagNameList) {
			queryWrapper = queryWrapper.like("tags", tagName);
		}
		return userMapper.selectList(queryWrapper).stream().map(this::getSafeUser).collect(Collectors.toList());
	}

	/**
	 * 用户信息脱敏
	 *
	 * @param originUser 原始用户信息
	 * @return 脱敏后的用户信息
	 */
	@Override
	public User getSafeUser(User originUser) {
		if (originUser == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "请求用户为空");
		}
		User safeUser = new User();
		safeUser.setId(originUser.getId());
		safeUser.setUsername(originUser.getUsername());
		safeUser.setUserAccount(originUser.getUserAccount());
		safeUser.setAvatarUrl(originUser.getAvatarUrl());
		safeUser.setGender(originUser.getGender());
		safeUser.setProfile(originUser.getProfile());
		safeUser.setTags(originUser.getTags());
		safeUser.setPhone(originUser.getPhone());
		safeUser.setEmail(originUser.getEmail());
		safeUser.setUserStatus(originUser.getUserStatus());
		safeUser.setCreateTime(originUser.getCreateTime());
		safeUser.setUserRole(originUser.getUserRole());
		safeUser.setPlanetCode(originUser.getPlanetCode());
		return safeUser;
	}

	/**
	 * 获取当前登录用户
	 *
	 * @param request HTTP请求对象
	 * @return 当前登录用户
	 */
	@Override
	public User getLoginUser(HttpServletRequest request) {
		if (request == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
		if (userObj == null) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
		}
		if (!(userObj instanceof User)) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
		}
		return (User) userObj;
	}

	/**
	 * 判断是否是管理员
	 *
	 * @param request 请求对象
	 * @return 判断结果
	 */
	@Override
	public Boolean isAdmin(HttpServletRequest request) {
		User safeUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
		return UserConstant.ADMIN_ROLE.equals(safeUser.getUserRole());
	}

	/**
	 * 判断是否是管理员
	 *
	 * @param loginUser 当前登录用户
	 * @return 判断结果
	 */
	@Override
	public Boolean isAdmin(User loginUser) {
		return UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
	}

	/**
	 * 分页获取推荐用户
	 *
	 * @param pageNum 当前页
	 * @param pageSize 每页显示条数
	 * @return 当前页的用户
	 */
	@Override
	public List<User> recommendUsers(Long pageNum, Long pageSize, HttpServletRequest request) {
		Gson gson = new Gson();
		// 获取缓存的key
		Long userId = getLoginUser(request).getId();
		String key = UserConstant.RECOMMEND_KEY + userId;
		// 查询缓存,判断是否缓存存在
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		String value = operations.get(key);
		if (value != null) {
			// 存在，经过反序列化，返回缓存
			List<User> users = gson.fromJson(value, new TypeToken<List<User>>(){}.getType());
			return users;
		}
		// 不存在则查询数据库
		// 创建查询条件
		QueryWrapper queryWrapper = new QueryWrapper();
		// mybatis plus根据条件分页查询
		Page page = new Page(pageNum, pageSize);
		Page<User> userPage = userMapper.selectPage(page, queryWrapper);
		// 获取分页对象中的用户数据并进行安全处理
		List<User> users = userPage.getRecords().stream().map(user -> getSafeUser(user)).collect(Collectors.toList());
		// 将处理后的用户数据序列化后存入缓存
		try {
			operations.set(key, gson.toJson(users), 30000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.error("Redis set key error.", e);
		}
		return users;
	}
}




