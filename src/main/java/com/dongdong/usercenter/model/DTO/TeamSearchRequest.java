package com.dongdong.usercenter.model.DTO;

import com.dongdong.usercenter.common.PageRequest;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * 搜索队伍请求参数包装类
 * @author Mr.Ye
 */
@Data
@Alias("TeamSearchRequest")
public class TeamSearchRequest extends PageRequest implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -6409320428411517880L;

    /**
     * 队伍ID
     */
    private Long id;

    /**
     * 搜索关键词，可以从队伍名称和描述中搜索
     */
    private String keyWords;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

}