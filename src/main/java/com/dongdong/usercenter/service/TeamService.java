package com.dongdong.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamJoinRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.DTO.TeamUpdateRequest;
import com.dongdong.usercenter.model.VO.TeamUserVO;
import com.dongdong.usercenter.model.domain.Team;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2022-09-25 20:21:56
 */
public interface TeamService extends IService<Team> {
	Long createTeam(TeamCreateRequest teamCreateRequest);

	List<TeamUserVO> searchTeams(TeamSearchRequest teamSearchRequest);

	void updateTeam(TeamUpdateRequest teamUpdateRequest);

	void joinTeam(TeamJoinRequest teamJoinRequest);

	void quitTeam(Long teamId);

	void deleteTeam(Long teamId);

	List<TeamUserVO> searchMyTeams(String keyWords);
}
