package com.dinz.library.dto;

import lombok.Data;

@Data
public class AdminOtherDTO {
	
	@Data
	public static class ChangePassword{
		private String oldPassword;
		private String newPassword;
	}
	
	@Data
	public static class ResetPasword{
		Long userId;
		private String password;
	}
}
