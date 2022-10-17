package com.dongdong.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.mapper.TeamMapper;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamJoinRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.DTO.TeamUpdateRequest;
import com.dongdong.usercenter.model.VO.TeamUserVO;
import com.dongdong.usercenter.model.domain.Team;
import com.dongdong.usercenter.model.domain.User;
import com.dongdong.usercenter.model.domain.UserTeam;
import com.dongdong.usercenter.model.enums.TeamStatusEnum;
import com.dongdong.usercenter.service.TeamService;
import com.dongdong.usercenter.service.UserService;
import com.dongdong.usercenter.service.UserTeamService;
import com.dongdong.usercenter.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

	@Resource
	private UserService userService;

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
		// TODO 有bug，可能同时创建100个队伍，考虑加锁解决，多机部署可用分布式锁。
		QueryWrapper<Team> wrapper = new QueryWrapper<>();
		final Long userId = user.getId();
		wrapper.eq("leader_id", userId);
		Long hasTeamNum = teamMapper.selectCount(wrapper);
		if (hasTeamNum >= 5) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "普通用户最多只能同时创建5个队伍");
		}
		// 4. 插入队伍信息到队伍表
		Team team = new Team();
		BeanUtils.copyProperties(teamCreateRequest, team);
		team.setLeaderId(userId);
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
	public List<TeamUserVO> searchTeams(TeamSearchRequest teamSearchRequest) {
		Integer status = teamSearchRequest.getStatus();
		if (status != null && status == 1 && !userService.isAdmin(UserHolder.getUser())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "私有队伍仅管理员允许查询!");
		}
		List<TeamUserVO> teams = teamMapper.searchTeams(teamSearchRequest);
		return teams;
	}

	/**
	 * 更新队伍信息
	 *
	 * @param teamUpdateRequest 更新队伍请求封装
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateTeam(TeamUpdateRequest teamUpdateRequest) {
		// 判空
		if (teamUpdateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "修改参数为空");
		}
		// 查询队伍是否存在
		Long teamId = teamUpdateRequest.getTeamId();
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
		}
		Team team = teamMapper.selectById(teamId);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "要修改的队伍不存在！");
		}
		// 只有管理员和队长可以修改
		User loginUser = UserHolder.getUser();
		if (loginUser == null || (!loginUser.getId().equals(team.getLeaderId()) && !userService.isAdmin(loginUser))) {
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "用户未登录或无权限！");
		}
		// 如果用户传入的新老值一致，无需修改
		if (team.getName().equals(teamUpdateRequest.getName()) && team.getDescription().equals(teamUpdateRequest.getDescription()) &&
				team.getMaxNum().equals(teamUpdateRequest.getMaxNum()) && team.getExpireTime().equals(teamUpdateRequest.getExpireTime()) &&
				team.getLeaderId().equals(teamUpdateRequest.getLeaderId()) && team.getStatus().equals(teamUpdateRequest.getStatus()) &&
				team.getPassword().equals(teamUpdateRequest.getPassword())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍信息无变更！");
		}
		// 如果队伍状态改为加密，必须要有密码
		if (TeamStatusEnum.SECRET.equals(TeamStatusEnum.getEnumByStatus(teamUpdateRequest.getStatus())) && StringUtils.isBlank(teamUpdateRequest.getPassword())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密队伍必须设置密码！");
		}
		teamMapper.updateTeam(teamUpdateRequest);
	}

	/**
	 * 加入队伍
	 *
	 * @param teamJoinRequest 加入队伍请求封装
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void joinTeam(TeamJoinRequest teamJoinRequest) {
		// 判空
		if (teamJoinRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "加入请求为空！");
		}
		// 队伍必须存在
		Long teamId = teamJoinRequest.getTeamId();
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在！");
		}
		Team team = teamMapper.selectById(teamId);
		if (team == null || LocalDateTime.now().isAfter(team.getExpireTime())) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在或已过期！");
		}
		// 不允许加入私有队伍
		TeamStatusEnum teamStatus = TeamStatusEnum.getEnumByStatus(team.getStatus());
		if (TeamStatusEnum.PRIVATE.equals(teamStatus)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法加入私有队伍！");
		}
		// 加密队伍必须要密码正确
		String joinPassword = teamJoinRequest.getPassword();
		if (TeamStatusEnum.SECRET.equals(teamStatus)) {
			if (StringUtils.isBlank(joinPassword) || !joinPassword.equals(team.getPassword())) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误！");
			}
		}
		// 队伍人数没有达到上限
		Integer hasJoinedUsers = team.getCurrentNum();
		if (hasJoinedUsers >= team.getMaxNum()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前队伍已满，请加入其他队伍！");
		}
		// 加入或创建的队伍最多5个
		Long userId = UserHolder.getUser().getId();
		QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		Long hasJoinedTeams = userTeamService.count(queryWrapper);
		if (hasJoinedTeams >= 5) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "普通用户最多可创建或加入5支队伍！");
		}
		// 不能重复加入队伍
		queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("team_id", teamId);
		queryWrapper.eq("user_id", userId);
		Long userHasJoinedTeams = userTeamService.count(queryWrapper);
		if (userHasJoinedTeams > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "不允许重复加入队伍！");
		}
		// 新增队伍和用户关联信息
		UserTeam userTeam = new UserTeam();
		userTeam.setUserId(userId);
		userTeam.setTeamId(teamId);
		userTeam.setJoinTime(LocalDateTime.now());
		Boolean result = userTeamService.save(userTeam);
		if (!result || userTeam.getId() == null) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "加入队伍失败！");
		}
		// 更新队伍人数
		team.setCurrentNum(hasJoinedUsers + 1);
		int updateNum = teamMapper.updateById(team);
		if (updateNum < 1) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍人数失败！");
		}
	}

	/**
	 * 退出队伍
	 *
	 * @param teamId 队伍ID
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void quitTeam(Long teamId) {
		// 校验队伍是否存在以及当前用户是否在队伍中
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在！");
		}
		User user = UserHolder.getUser();
		Long userId = user.getId();
		QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("team_id", teamId);
		long count = userTeamService.count(queryWrapper);
		Team team = teamMapper.selectById(teamId);
		if (team == null || count == 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在或用户还未加入此队伍！");
		}
		// 队伍人数 -1，再判断如果队伍只剩一人，解散队伍
		Integer currentNum = team.getCurrentNum();
		Long leaderId = team.getLeaderId();
		team.setCurrentNum(currentNum - 1);
		int updateNum = 0;
		if (currentNum == 1) {
			// 更新队伍人数
			updateNum = teamMapper.updateById(team);
			int deleteNum = teamMapper.deleteById(teamId);
			if (deleteNum < 1) {
				throw new BusinessException(ErrorCode.SYSTEM_ERROR, "解散队伍失败！");
			}
		} else {
			// 队伍不止一人，如果是队长退出，权限移交给第二早加入的人
			if (userId.equals(leaderId)) {
				queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("team_id", teamId);
				queryWrapper.last("order by join_time asc limit 2");
				List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
				if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
					throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍人数不足2人！");
				}
				Long nextTeamLeaderId = userTeamList.get(1).getUserId();
				// 设置teamLeader
				team.setLeaderId(nextTeamLeaderId);
				// 更新队伍人数和teamLeader
				updateNum = teamMapper.updateById(team);
			}
		}
		// 移除关联关系
		boolean removeNum = userTeamService.remove(queryWrapper);
		// 判断是否更新成功
		if (updateNum < 1 || !removeNum) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新信息失败！");
		}
	}

	/**
	 * 解散队伍
	 *
	 * @param teamId 队伍ID
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteTeam(Long teamId) {
		// 1. 校验队伍是否存在
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在！");
		}
		Team team = teamMapper.selectById(teamId);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
		}
		// 2. 当前用户是队长
		User user = UserHolder.getUser();
		if (!team.getLeaderId().equals(user.getId())){
			throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "仅队长允许解散队伍！");
		}
		// 3. 删除关联关系
		QueryWrapper<UserTeam> queryWrapper = new QueryWrapper();
		queryWrapper.eq("team_id", teamId);
		boolean remove = userTeamService.remove(queryWrapper);
		if (!remove){
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "移除关联关系失败！");
		}
		// 4. 更新队伍人数并删除队伍
		team.setCurrentNum(0);
		int updateNum = teamMapper.updateById(team);
		int deleteNum = teamMapper.deleteById(teamId);
		if (updateNum < 1 || deleteNum < 1) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败！");
		}
	}
}




