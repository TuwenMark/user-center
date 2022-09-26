package com.dongdong.usercenter.job;

import com.dongdong.usercenter.constant.UserConstant;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program: user-center
 * @description: Redisson工具的测试类
 * @author: Mr.Ye
 * @create: 2022-09-25 17:29
 **/
@SpringBootTest
public class RedissonTest {
	@Resource
	private RedissonClient redissonClient;

	@Test
	void testWatchDog() {
		RLock lock = redissonClient.getLock(UserConstant.PRECACHE_LOCK_KEY);
		try {
			if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
				System.out.println("我抢到锁啦！！！");
				System.out.println("getLock:" + Thread.currentThread().getId());
				Thread.sleep(200000);
			}
		} catch (InterruptedException e) {
			System.out.println("获取锁报错！！！");
		} finally {
			// 释放自己的锁
			if (lock.isHeldByCurrentThread()) {
				System.out.println("unLock:" + Thread.currentThread().getId());
				lock.unlock();
			}
		}
	}
}
