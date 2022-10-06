package com.dongdong.usercenter.model.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 加入队伍请求封装类
 *
 * @author Mr.Ye
 */
@Data
public class TeamJoinRequest implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 3399082725961425636L;

    /**
     * 队伍ID
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;

}