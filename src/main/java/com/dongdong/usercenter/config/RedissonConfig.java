package com.dongdong.usercenter.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: user-center
 * @description: Redisson配置类
 * @author: Mr.Ye
 * @create: 2022-09-25 11:13
 **/
@Configuration()
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

	private String host;

	private String port;

	@Bean
	public RedissonClient redissonClient() {
		// 1. Create config object
		Config config = new Config();
		String redisAddress = String.format("redis://%s:%s", host, port);
		config.useSingleServer().setAddress(redisAddress).setPassword("123456").setDatabase(1);
		// 2. Create Redisson instance
		return  Redisson.create(config);
	}
}

