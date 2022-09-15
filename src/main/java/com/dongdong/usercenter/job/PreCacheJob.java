package com.dongdong.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongdong.usercenter.constant.UserConstant;
import com.dongdong.usercenter.mapper.UserMapper;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: user-center
 * @description: 缓存预热类
 * @author: Mr.Ye
 * @create: 2022-09-15 21:40
 **/
@Component
@Slf4j
public class PreCacheJob {
	@Resource
	private UserMapper userMapper;

	@Resource
	private UserService userService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 核心用户列表
	 */
	private List<Long> hotUserList = Arrays.asList(6L);

	@Scheduled(cron = "0 21 22 * * ?")
	private void doCacheRecommendUsers() {
		System.out.println("缓存预热开始！");
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		Gson gson = new Gson();
		hotUserList.forEach(userId -> {
			String key = UserConstant.RECOMMEND_KEY + userId;
			// 创建查询条件
			QueryWrapper queryWrapper = new QueryWrapper();
			// mybatis plus根据条件分页查询
			Page page = new Page(1, 20);
			Page<User> userPage = userMapper.selectPage(page, queryWrapper);
			// 获取分页对象中的用户数据并进行安全处理
			List<User> users = userPage.getRecords().stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
			// 将处理后的用户数据序列化后存入缓存
			try {
				operations.set(key, gson.toJson(users), 30000, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				log.error("Redis set key error.", e);
			}
		});
		System.out.println("缓存预热完毕！");
	}
}
