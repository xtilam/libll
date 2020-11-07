package com.dinz.library.dto;

import com.dinz.library.model.Permission;

import lombok.Data;

@Data
public class PermissionDTO {
	private Long id;
	private String permissionCode;
	private String description;
	private String name;

	public Permission convert() {
		Permission permission = new Permission();
		permission.setPermissionCode(this.permissionCode);
		permission.setName(this.getName());
		permission.setDescription(this.getDescription());
		permission.setId(this.id);
		return permission;
	}

}