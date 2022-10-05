package com.dongdong.usercenter.model.VO;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 队伍信息中展示的用户相关信息
 *
 * @author Mr.Ye
 */
@Data
@Alias("UserVO")
public class UserVO implements Serializable {

    /**
	 * 序列化
     */
    private static final long serialVersionUID = 361680310313760401L;

    /**
     * 用户唯一标识
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

}