package com.dongdong.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.VO.TeamSearchResponse;
import com.dongdong.usercenter.model.domain.Team;

import java.util.List;

/**
 * @author Admin
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2022-09-25 20:21:56
 */
public interface TeamService extends IService<Team> {
	Long createTeam(TeamCreateRequest teamCreateRequest);

	List<TeamSearchResponse> searchTeams(TeamSearchRequest teamSearchRequest);
}
