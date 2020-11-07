/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Lưu lại phiên lần cuối sử dụng của người dùng
 * 
 * @author dinzenida
 */
@Data
@AllArgsConstructor
public class AdminLoginInfo {

	private Long lastUsed;
	private String ipAddress;
	private String userAgent;

	public AdminLoginInfo(String ipAddress, String userAgent) {
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
	}

	public AdminLoginInfo(HttpServletRequest req) {
		this.updateToken(req);
	}

	public void updateToken(HttpServletRequest req) {
		this.ipAddress = req.getRemoteAddr();
		this.userAgent = req.getHeader("User-Agent");
		this.lastUsed = System.currentTimeMillis();
	}
}
