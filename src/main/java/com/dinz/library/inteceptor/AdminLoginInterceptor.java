package com.dinz.library.inteceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.cache.PermissionCache;
import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.jwt.AdminJWTModel;
import com.dinz.library.jwt.AdminJWTProvider;

@Component
public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

	private static final String accessDenied = new JSONObject(
			new APIResult(new APIResultMessage(-1, "Access denied"), "")).toString();
	private static final String accessDeniedPermission = new JSONObject(
			new APIResult(new APIResultMessage(-2, "Tài khoản không có quyền thực hiện hành động này"), "")).toString();

	@Autowired
	PermissionCache permissionCache;

	@Autowired
	AdminUserLoginCache adminUserLoginCache;

	@Autowired
	AdminJWTProvider adminJWTProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("Authorization"); 
		
		if(null != request.getHeader("Origin")) {
			setAccessControlHeaders(response);
		}
		
		String error = accessDenied;
		HttpSession hSession = request.getSession();
		if (null != token) { 
			// kiểm tra token hợp lệ
			AdminJWTModel admin = adminJWTProvider.decodeToken(token);
			if (null != admin) {
				hSession.setAttribute("adminId", admin.getUserId());
				hSession.setAttribute("adminCode", admin.getUserCode());
				hSession.setAttribute("tokenId", admin.getTokenId()); 
				// token còn sống trong cache
				boolean isTokenExits = adminUserLoginCache.checkTokenValidAndUpdateIt(); // use session autowired
				if (isTokenExits) {
					// full permission account
					if(admin.getUserId() < 0) {
						return true;
					}
					Long permissionIdInCache = permissionCache.getPermissionId(); // use request autowired
					if (null != permissionIdInCache) { 
						boolean userCanAccess = adminUserLoginCache.getPermissions().contains(permissionIdInCache); // use session autowired
						if (userCanAccess) {
							return true;
						} else {
							error = accessDeniedPermission;  
						}
					} else {
						return true;
					}
				}
			}
		}
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		setAccessControlHeaders(response);
		writer.print(error);
		writer.flush();
		return false;
	}

	private void setAccessControlHeaders(HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		resp.setHeader("Access-Control-Allow-Headers", "*");
		resp.setHeader("Access-Control-Allow-Methods", "*");
	}

}
