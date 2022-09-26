package com.dongdong.usercenter.model.DTO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 队伍请求参数包装类
 * @author Mr.Ye
 */
@Data
public class TeamCreateRequest implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -3880080545314712926L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 创建人用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}