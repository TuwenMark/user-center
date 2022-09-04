package com.dongdong.usercenter.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: user-center
 * @description: Excel中的用户对象
 * @author: Mr.Ye
 * @create: 2022-08-28 18:03
 **/
@Data
public class UserInfo {
	@ExcelProperty("用户昵称")
	private String username;

	@ExcelProperty("星球编号")
	private Integer planetCode;
}
