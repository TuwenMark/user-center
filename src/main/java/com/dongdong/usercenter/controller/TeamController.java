package com.dongdong.usercenter.controller;

import com.dongdong.usercenter.common.BaseResponse;
import com.dongdong.usercenter.common.ErrorCode;
import com.dongdong.usercenter.exception.BusinessException;
import com.dongdong.usercenter.model.DTO.TeamCreateRequest;
import com.dongdong.usercenter.model.DTO.TeamJoinRequest;
import com.dongdong.usercenter.model.DTO.TeamSearchRequest;
import com.dongdong.usercenter.model.DTO.TeamUpdateRequest;
import com.dongdong.usercenter.model.VO.TeamUserVO;
import com.dongdong.usercenter.model.domain.Team;
import com.dongdong.usercenter.service.TeamService;
import com.dongdong.usercenter.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: user-center
 * @description: 组队功能控制层接口
 * @author: Mr.Ye
 * @create: 2022-09-25 22:12
 **/
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
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
	 * 修改队伍
	 *
	 * @param teamUpdateRequest 修改队伍请求对象
	 * @return 修改结果
	 */
	@PutMapping("/update")
	public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest) {
		// 判空
		if (teamUpdateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		teamService.updateTeam(teamUpdateRequest);
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
	 * 根据条件分页查询所有队伍
	 *
	 * @param teamSearchRequest 队伍请求对象
	 * @return 查询结果
	 */
	@GetMapping("/list")
	public BaseResponse<List<TeamUserVO>> searchTeams(@RequestParam TeamSearchRequest teamSearchRequest) {
		// 判空
		if (teamSearchRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		List<TeamUserVO> teams = teamService.searchTeams(teamSearchRequest);
		return ResponseUtils.success(teams);
	}

	/**
	 * 搜索我加入的队伍
	 *
	 * @param keyWords	搜索条件，可为空
	 * @return 已加入的队伍列表
	 */
	@GetMapping("/list/my")
	public BaseResponse<List<TeamUserVO>> searchMyTeams(@RequestParam String keyWords) {
		// 判空，如果关键词为空，设为null，不作为查询条件
		if (StringUtils.isBlank(keyWords)) {
			keyWords = null;
		}
		List<TeamUserVO> teams = teamService.searchMyTeams(keyWords);
		return ResponseUtils.success(teams);
	}

	/**
	 * 加入队伍
	 *
	 * @param teamJoinRequest 加入队伍请求封装类
	 * @return 是否成功加入
	 */
	@PostMapping("/join")
	public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest) {
		// 判空
		if (teamJoinRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		teamService.joinTeam(teamJoinRequest);
		return ResponseUtils.success(true);
	}

	/**
	 * 退出队伍
	 *
	 * @param teamId 队伍ID
	 * @return 是否退出队伍
	 */
	@GetMapping("/quit")
	public BaseResponse<Boolean> quitTeam(@RequestParam Long teamId) {
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在！");
		}
		teamService.quitTeam(teamId);
		return ResponseUtils.success(true);
	}

	/**
	 * 解散队伍
	 *
	 * @param teamId 队伍ID
	 * @return 解散结果
	 */
	@DeleteMapping("/delete")
	public BaseResponse<Boolean> deleteTeam(@RequestParam Long teamId) {
		// 判空
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误！");
		}
		// 查找是否存在此队伍
		teamService.deleteTeam(teamId);
		return ResponseUtils.success(true);
	}

}
