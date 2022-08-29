package com.dongdong.usercenter.excel;

import com.alibaba.excel.EasyExcel;

public class ExcelRead {
	/**
	 * 最简单的读
	 *
	 * 1. 创建excel对应的实体对象
	 *
	 * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
	 *
	 * 3. 直接读即可
	 */
	public static void main(String[] args) {
		// 写法1：JDK8+ ,不用额外写一个DemoDataListener
		// since: 3.0.0-beta1
		String fileName = "E:\\projects\\user-center\\src\\main\\resources\\user.xlsx";
		// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
		// 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
		EasyExcel.read(fileName, UserInfo.class, new UserListener()).sheet().doRead();
	}
}
