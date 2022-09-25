package com.dongdong.usercenter.model.domain.DTO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户登录账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户性别，"0" 男性，"1" 女性
     */
    private Integer gender;

    /**
     * 用户自我介绍
     */
    private String profile;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 星球编号
     */
    @Positive
    private Integer planetCode;

    /**
     * 标签列表 JSON
     */
    private String tags;

}