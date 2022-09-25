package com.dongdong.usercenter.model.domain.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: user-center
 * @description: 用户登录实体类
 * @author: Mr.Ye
 * @create: 2022-05-14 18:10
 **/
@Data
public class UserLoginRequest implements Serializable {

	private static final long serialVersionUID = -1038517441080795893L;

	private String userAccount;

	private String userPassword;

	private String phoneNumber;

	private String code;
}
