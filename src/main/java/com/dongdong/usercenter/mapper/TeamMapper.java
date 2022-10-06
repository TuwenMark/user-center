package com.dongdong.usercenter.mapper;

import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.DTO.TeamUpdateRequest;
import com.dongdong.usercenter.model.VO.TeamUserVO;
import com.dongdong.usercenter.model.domain.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Admin
* @description 针对表【team(队伍)】的数据库操作Mapper
* @createDate 2022-09-25 20:21:56
* @Entity com.dongdong.usercenter.model.domain.Team
*/
public interface TeamMapper extends BaseMapper<Team> {

	/**
	 * 根据条件查询队伍列表
	 *
	 * @param teamSearchRequest 查询队伍列表条件包装类
	 * @return 符合条件的队伍列表
	 */
	List<TeamUserVO> searchTeams(TeamSearchRequest teamSearchRequest);

	void updateTeam(TeamUpdateRequest teamUpdateRequest);
}




