package com.dongdong.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class UserCenterApplicationTests {

	@Test
	void testEncrypt() {
		String result = DigestUtils.md5DigestAsHex("yupi1223".getBytes(StandardCharsets.UTF_8));
		System.out.println(result);

	}

	@Test
	void contextLoads() {
	}

}
