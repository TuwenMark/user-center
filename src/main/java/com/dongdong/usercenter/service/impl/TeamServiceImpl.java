package com.dongdong.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.mapper.TeamMapper;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.VO.TeamSearchResponse;
import com.dongdong.usercenter.model.domain.Team;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.model.domain.UserTeam;
import com.dongdong.usercenter.model.enums.TeamStatusEnum;
import com.dongdong.usercenter.service.TeamService;
import com.dongdong.usercenter.service.UserTeamService;
import com.dongdong.usercenter.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Admin
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2022-09-25 20:21:56
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

	@Resource
	private TeamMapper teamMapper;

	@Resource
	private UserTeamService userTeamService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Long createTeam(TeamCreateRequest teamCreateRequest) {
		// 1. 请求参数是否为空？
		if (teamCreateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍信息不能为空");
		}
		// 2. 是否登录，未登录不允许创建
		User user = UserHolder.getUser();
		if (user == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录，请重新登录");
		}
		// 3. 校验信息
		// 3.1. 队伍人数 > 1 且 <= 20
		Integer maxNum = Optional.ofNullable(teamCreateRequest.getMaxNum()).orElse(0);
		if (maxNum <= 1 || maxNum > 20) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不符合要求");
		}
		// 3.2. 队伍标题 <= 20
		String teamName = teamCreateRequest.getName();
		if (StringUtils.isBlank(teamName) || teamName.length() > 20) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不符合要求");
		}
		// 3.3. 描述 <= 512
		String description = teamCreateRequest.getDescription();
		if (StringUtils.isNotBlank(description) && description.length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不符合要求");
		}
		// 3.4. status 是否公开（int）不传默认为 0（公开）
		Integer status = Optional.ofNullable(teamCreateRequest.getStatus()).orElse(0);
		TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByStatus(status);
		if (statusEnum == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不符合要求");
		}
		// 3.5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
		String password = teamCreateRequest.getPassword();
		if (TeamStatusEnum.SECRET.equals(statusEnum)) {
			if (StringUtils.isBlank(password) || password.length() > 32) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不符合要求");
			}
		}
		// 3.6. 超时时间 > 当前时间
		LocalDateTime expireTime = teamCreateRequest.getExpireTime();
		if (expireTime == null || LocalDateTime.now().isAfter(expireTime)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍过期时间不符合要求");
		}
		// 3.7. 校验用户最多创建 5 个队伍
		QueryWrapper<Team> wrapper = new QueryWrapper<>();
		Long userId = user.getId();
		wrapper.eq("user_id", userId);
		Long hasTeamNum = teamMapper.selectCount(wrapper);
		if (hasTeamNum >= 5) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "普通用户最多只能同时创建5个队伍");
		}
		// 4. 插入队伍信息到队伍表
		Team team = new Team();
		BeanUtils.copyProperties(teamCreateRequest, team);
		team.setUserId(userId);
		int result = teamMapper.insert(team);
		Long teamId = team.getId();
		if (result < 0 || teamId == null) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
		}
		//5. 插入用户  => 队伍关系到关系表
		UserTeam userTeam = new UserTeam();
		userTeam.setUserId(userId);
		userTeam.setTeamId(teamId);
		userTeam.setJoinTime(LocalDateTime.now());
		boolean res = userTeamService.save(userTeam);
		if (!res || userTeam.getId() == null) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
		}
		return teamId;
	}

	/**
	 * 根据条件查询队伍列表
	 *
	 * @param teamSearchRequest 查询队伍列表条件包装类
	 * @return 符合条件的队伍列表
	 */
	@Override
	public List<TeamSearchResponse> searchTeams(TeamSearchRequest teamSearchRequest) {
		List<TeamSearchResponse> teams = teamMapper.searchTeams(teamSearchRequest);
		System.out.println(teams);
		return teams;
	}
}




