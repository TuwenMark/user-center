package com.dongdong.usercenter.utils;

import java.util.List;

/**
 * @program: user-center
 * @description: 算法工具类
 * @author: Mr.Ye
 * @create: 2022-10-17 07:40
 **/
public class AlgorithmUtils {
	private AlgorithmUtils() {
	}

	/**
	 * 最短编辑距离算法，变更次数越小，两个字符串越接近（如果tag数量相等，与顺序有关，如果不相等，与顺序无关）
	 *
	 * @param word1 字符串1
	 * @param word2 字符串2
	 * @return 变更次数
	 */
	public static int minDistance(String word1, String word2) {
		int n = word1.length();
		int m = word2.length();

		if (n * m == 0) {
			return n + m;
		}
		int[][] d = new int[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			d[i][0] = i;
		}

		for (int j = 0; j < m + 1; j++) {
			d[0][j] = j;
		}

		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < m + 1; j++) {
				int left = d[i - 1][j] + 1;
				int down = d[i][j - 1] + 1;
				int leftDown = d[i - 1][j - 1];
				if (word1.charAt(i - 1) != (word2.charAt(j - 1))) {
					leftDown += 1;
				}
				d[i][j] = Math.min(left, Math.min(down, leftDown));
			}
		}
		return d[n][m];
	}

	/**
	 * 最短编辑距离算法，变更次数越小，两个列表越接近
	 *
	 * @param wordList1 列表1
	 * @param wordList2 列表2
	 * @return 变更次数
	 */
	public static int minDistance(List<String> wordList1, List<String> wordList2) {
		int n = wordList1.size();
		int m = wordList2.size();

		if (n * m == 0) {
			return n + m;
		}
		int[][] d = new int[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			d[i][0] = i;
		}

		for (int j = 0; j < m + 1; j++) {
			d[0][j] = j;
		}

		for (int i = 1; i < n + 1; i++) {
			for (int j = 1; j < m + 1; j++) {
				int left = d[i - 1][j] + 1;
				int down = d[i][j - 1] + 1;
				int leftDown = d[i - 1][j - 1];
				if (!wordList1.get(i - 1).equals(wordList2.get(j - 1))) {
					leftDown += 1;
				}
				d[i][j] = Math.min(left, Math.min(down, leftDown));
			}
		}
		return d[n][m];
	}
}
