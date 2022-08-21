package com.dongdong.usercenter.service;

import com.dongdong.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserServiceTest {
	@Resource
	private UserService userService;

	@Test
	void testAddUser() {
		User user = new User();
		user.setUsername("yupi1");
		user.setUserAccount("1234567");
		user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/cloudbase-cms/upload/2021-02-05/wpd5cu8cpudb38hjy3rckjaxwg3c4ixj_.png");
		user.setGender(0);
		user.setUserPassword("456");
		user.setPhone("123456");
		user.setEmail("2345@qq.com");

		boolean result = userService.save(user);
		System.out.println(user.getId());
		Assertions.assertTrue(result);

	}

	@Test
	void userRegister() {
		// 正常情况
		String userAccount = "test01";
		String userPassword = "1234567890";
		String checkPassword = "1234567890";
		Integer planetCode = 1;
		Long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(31L, result);
		// 账号为空
		userAccount = "";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
		// 账户小于4位
		userAccount = "dog";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
		// 账户包含特殊字符
		userAccount = "dog$";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
		// 密码小于8位
		userAccount = "dog1";
		userPassword = "1234567";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
		// 密码和校验密码不同
		userPassword = "12345678";
		checkPassword = "12435678";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
		// 账户重复
		userAccount = "yupi";
		userPassword = "1234567890";
		checkPassword = "1234567890";
		result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		Assertions.assertEquals(-1L, result);
	}

	@Test
	void searchUsersByTags() {
		List<String> tagNameList = Arrays.asList("Java", "C++", "Python");
		List<User> userList = userService.searchUsersByTags(tagNameList);
		Assertions.assertNotNull(userList);
	}
}