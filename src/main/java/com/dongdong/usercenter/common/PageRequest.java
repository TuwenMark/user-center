package com.dongdong.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: user-center
 * @description: 分页请求对象
 * @author: Mr.Ye
 * @create: 2022-09-25 23:04
 **/
@Data
public class PageRequest implements Serializable {

	/**
	 * 序列化ID,使对象在序列化的时候保证唯一
	 */
	private static final long serialVersionUID = 6903510127586079902L;

	/**
	 * 当前所在页数
	 */
	protected int pageNum = 1;

	/**
	 * 每页显示条数
	 */
	protected int pageSize = 10;
}
