package com.dongdong.usercenter.model.domain;

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
public class User implements Serializable {
    /**
     * 用户唯一标识
     */
    @TableId(type = IdType.AUTO)
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
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户状态，"0" 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除，"0" 未删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0 —— 默认用户 1 ——管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    @Positive
    private Integer planetCode;

    /**
     * 标签列表 JSON
     */
    private String tags;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}