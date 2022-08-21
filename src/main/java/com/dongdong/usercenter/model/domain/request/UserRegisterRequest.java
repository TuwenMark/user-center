package com.dongdong.usercenter.model.domain.request;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * @program: user-center
 * @description: 用户注册请求体实体类
 * @author: Mr.Ye
 * @create: 2022-05-14 17:45
 **/
@Data
public class UserRegisterRequest implements Serializable {

	private static final long serialVersionUID = -3461337243308497148L;

	private String userAccount;

	private String userPassword;

	private String checkPassword;

	@Positive
	private Integer planetCode;
}
