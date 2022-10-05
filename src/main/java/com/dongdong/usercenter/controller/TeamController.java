package com.dongdong.usercenter.controller;

import com.dongdong.usercenter.common.BaseResponse;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.VO.TeamSearchResponse;
import com.dongdong.usercenter.model.domain.Team;
import com.dongdong.usercenter.service.TeamService;
import com.dongdong.usercenter.utils.ResponseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: user-center
 * @description: 组队功能控制层接口
 * @author: Mr.Ye
 * @create: 2022-09-25 22:12
 **/
@RestController
@RequestMapping("/team")
public class TeamController {

	@Resource
	private TeamService teamService;

	/**
	 * 创建队伍
	 *
	 * @param teamCreateRequest 队伍请求对象
	 * @return 创建成功的队伍ID
 	 */
	@PostMapping("/create")
	public BaseResponse<Long> createTeam(@RequestBody TeamCreateRequest teamCreateRequest) {
		// 判空
		if (teamCreateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		// 插入数据
		Long teamId = teamService.createTeam(teamCreateRequest);
		return ResponseUtils.success(teamId);
	}

	/**
	 * 删除队伍
	 *
	 * @param id 队伍ID
 	 * @return 删除结果
	 */
	@DeleteMapping("/delete")
	public BaseResponse<Boolean> deleteTeam(@RequestParam Long id) {
		// 判空
		if (id == null || id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		// 查找是否存在此队伍
		Team team = teamService.getById(id);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
		}
		// 删除队伍并判断删除结果
		if (!teamService.removeById(id)) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败！");
		}
		return ResponseUtils.success(true);
	}

	/**
	 * 修改队伍
	 *
	 * @param teamRequest 队伍请求对象
	 * @return 修改结果
	 */
	@PutMapping("/update")
	public BaseResponse<Boolean> updateTeam(@RequestBody TeamRequest teamRequest) {
		// 判空
		if (teamRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		// 查找是否存在此队伍
		Team team = teamService.getById(teamRequest.getId());
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
		}
		// 修改队伍并判断修改结果
		teamRequest.setUpdateTime(LocalDateTime.now());
		// TODO teamRequest中为空的字段会有默认值，这样会覆盖数据库中的数据
		BeanUtils.copyProperties(teamRequest, team);
		if (!teamService.updateById(team)) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改队伍失败！");
		}
		return ResponseUtils.success(true);
	}

	/**
	 * 根据ID查找单个队伍
	 *
	 * @param id 队伍ID
	 * @return 对应ID的队伍
	 */
	@GetMapping("/query")
	public BaseResponse<Team> queryTeamById(@RequestParam Long id) {
		// 判空
		if (id == null || id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		// 查找队伍
		Team team = teamService.getById(id);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
		}
		return ResponseUtils.success(team);
	}

	/**
	 * 根据条件分页查询队伍
	 *
	 * @param teamSearchRequest 队伍请求对象
	 * @return 查询结果
	 */
	@GetMapping("/search/list")
	public BaseResponse<List<TeamSearchResponse>> searchTeams(TeamSearchRequest teamSearchRequest) {
		// 判空
		if (teamSearchRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		List<TeamSearchResponse> teams = teamService.searchTeams(teamSearchRequest);
		return ResponseUtils.success(teams);
	}


}
