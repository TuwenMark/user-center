package com.dongdong.usercenter.model.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 搜索队伍返回对象包装类
 *
 * @author Mr.Ye
 */
@Data
@Alias("TeamUserVO")
public class TeamUserVO implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -7780513556148917961L;

    /**
     * id
     */
    private Long teamId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 成员列表：首位的是创建人
     */
    private List<UserVO> userVOList;

}