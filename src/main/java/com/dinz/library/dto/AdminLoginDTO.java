package com.dinz.library.dto;

import com.dinz.library.model.Admin;

import lombok.Data;

@Data
public class AdminLoginDTO {
	private String username;
	private String email;
	private String password;
	
	public Admin toAdmin() {
		Admin admin = new Admin();
		admin.setAdminCode(this.username);
		admin.setPassword(password);
		admin.setEmail(email);
		return admin;
	}
}
