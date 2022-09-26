package com.dongdong.usercenter.model.enums;

/**
 * @program: user-center
 * @description: 队伍状态枚举
 * @author: Mr.Ye
 * @create: 2022-09-26 22:14
 **/
public enum TeamStatusEnum {

	/**
	 * 公开状态
	 */
	PUBLIC(0, "公开"),

	/**
	 * 私有状态
	 */
	PRIVATE(1, "私有"),

	/**
	 * 加密状态，即有密码
	 */
	SECRET(2, "加密");

	private Integer status;

	private String description;

	TeamStatusEnum(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static TeamStatusEnum getEnumByStatus(Integer status) {
		if (status == null) {
			return null;
		}
		for (TeamStatusEnum teamStatusEnum : values()) {
			if (teamStatusEnum.getStatus().equals(status)) {
				return teamStatusEnum;
			}
		}
		return null;
	}
}
