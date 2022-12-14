package com.dongdong.usercenter.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 创建队伍请求参数包装类
 * @author Mr.Ye
 */
@Data
public class TeamCreateRequest implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -3880080545314712926L;

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
     * 过期时间，前端必须按照这个格式传递时间的JSON字符串
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;
}