package com.dinz.library;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		if (auth != null) {
			if (auth.getPrincipal() instanceof AdminUserDetails) {
				AdminUserDetails adminDetail = (AdminUserDetails) auth.getPrincipal();
				// Tài khoản admin có id < 0 -> full quyền
				if (adminDetail.getAdmin().getId() > 0) {
					return (adminDetail.checkPermission(permission.toString()));
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		throw new UnsupportedOperationException();
	}

}
