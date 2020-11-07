/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dinz.library.AdminUserDetails;
import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.constants.SystemConstants;
import com.dinz.library.model.Admin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author dinzenida
 */
@Component
public class AdminJWTProvider {

	@Autowired
	AdminUserLoginCache adminCache;

	private static Long MAX_TIME = 1000L * 3600 * 24 * 365 * 10;

	public String generateToken(Admin admin, Long tokenId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", admin.getId());
		map.put("adminCode", admin.getAdminCode());
		map.put("tokenId", tokenId);
		return this.generateToken(map);
	}

	public String generateToken(Map<String, Object> adminMap, Long tokenId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", adminMap.get("id"));
		map.put("code", adminMap.get("adminCode"));
		map.put("tokenId", tokenId);
		return this.generateToken(map);
	}

	private String generateToken(Map<String, Object> mapClaims) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + MAX_TIME);

		String jwt = Jwts.builder()//
				.setClaims(mapClaims).setIssuedAt(now)//
				.setExpiration(expiryDate)//
				.signWith(SignatureAlgorithm.HS512, SystemConstants.ADMIN_JWT_SECRET)//
				.compact();
		return jwt;
	}

	public UserDetails decodeToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SystemConstants.ADMIN_JWT_SECRET).parseClaimsJws(token)
					.getBody();
			AdminUserDetails adminUserDetails = new AdminUserDetails(
					new Admin(new Long(claims.get("id").toString()), (String) claims.get("code")), //
					adminCache, new Long(claims.get("tokenId").toString()));
			return adminUserDetails;
		} catch (Exception ex) {
		}
		return null;
	}
}
