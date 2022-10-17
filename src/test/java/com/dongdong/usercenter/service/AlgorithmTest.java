package com.dongdong.usercenter.service;

import com.dongdong.usercenter.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @program: user-center
 * @description: 算法测试工具类
 * @author: Mr.Ye
 * @create: 2022-10-17 07:44
 **/
@SpringBootTest
public class AlgorithmTest {

	@Test
	void testMinDistance1() {
		String word1 = "rose";
		String word2 = "Rose";
		String word3 = "ro";
		String word4 = "cat";

		int res1 = AlgorithmUtils.minDistance(word1, word2);
		int res2 = AlgorithmUtils.minDistance(word1, word3);
		int res3 = AlgorithmUtils.minDistance(word1, word4);

		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);
	}

	@Test
	void testMinDistance2(){
		List<String> list1 = Arrays.asList("PHP","女","乒乓球","Python","大四");
		List<String> list2 = Arrays.asList("女","C++","Java","Python","本科");
		List<String> list3 = Arrays.asList("女","Python","Go");
		List<String> list4 = Arrays.asList("女","C++","Java","Python","大一");

		int res1 = AlgorithmUtils.minDistance(list1, list2);
		int res2 = AlgorithmUtils.minDistance(list1, list3);
		int res3 = AlgorithmUtils.minDistance(list1, list4);
		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);
	}
}
