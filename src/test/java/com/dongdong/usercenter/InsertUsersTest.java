package com.dongdong.usercenter;

import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @program: user-center
 * @description: 批量插入用户测试
 * @author: Mr.Ye
 * @create: 2022-09-08 22:01
 **/
@SpringBootTest
public class InsertUsersTest {
	@Resource
	private UserService userService;

	/**
	 * 自定义线程池
	 */
	private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 10000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

	/**
	 * 批量插入用户
	 */
	@Test
	void insertUsers() {
		ArrayList<User> userList = new ArrayList<>();
		int num = 2000;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 1000; i < num; i++) {
			User user = new User();
			user.setUsername("鱼皮" + i);
			user.setUserAccount("鱼皮" + i);
			user.setAvatarUrl("https://i2.hdslb.com/bfs/face/cb9ef82714507e6bda707dac216da94c97d70037.jpg@240w_240h_1c_1s.webp");
			user.setGender(0);
			user.setProfile("我是鱼皮" + i + "请多多关照呀~");
			user.setUserPassword("b0dd3697a192885d7c055db46155b26a");
			user.setPhone("1234567890");
			user.setEmail("12345@163.com");
			user.setUserStatus(0);
			user.setIsDelete(0);
			user.setUserRole(0);
			user.setPlanetCode(15 + i);
			user.setTags("[\"女\",\"C++\",\"Java\",\"Python\",\"大一\"]");
			userList.add(user);
		}
		userService.saveBatch(userList, 100);
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeSeconds());
	}


	/**
	 * 异步批量插入用户
	 */
	@Test
	void insertConcurrencyUsers() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ArrayList<Future> futureList = new ArrayList<>();
		int currentNum = 482014;
		int batchSize = 10000;
		int j = 0;
		for (int i = 0; i < 10; i++) {
			ArrayList<User> userList = new ArrayList<>();
			while (true) {
				j++;
				User user = new User();
				user.setUsername("鱼皮" + (currentNum + j));
				user.setUserAccount("鱼皮" + (currentNum + j));
				user.setAvatarUrl("https://i2.hdslb.com/bfs/face/cb9ef82714507e6bda707dac216da94c97d70037.jpg@240w_240h_1c_1s.webp");
				user.setGender(0);
				user.setProfile("我是鱼皮" + (currentNum + j) + "请多多关照呀~");
				user.setUserPassword("b0dd3697a192885d7c055db46155b26a");
				user.setPhone("1234567890");
				user.setEmail("12345@163.com");
				user.setUserStatus(0);
				user.setIsDelete(0);
				user.setUserRole(0);
				user.setPlanetCode(currentNum + j);
				user.setTags("[\"女\",\"C++\",\"Java\",\"Python\",\"大一\"]");
				userList.add(user);
				if (j % batchSize == 0) {
					break;
				}
			}
			currentNum += batchSize;
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				System.out.println("threadName = " + Thread.currentThread().getName());
				userService.saveBatch(userList, batchSize);
			}, executorService);
			futureList.add(future);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeSeconds());
	}
}
