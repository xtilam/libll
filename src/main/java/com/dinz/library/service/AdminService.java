package com.dinz.library.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dinz.library.model.Admin;

public abstract class AdminService {
	public abstract List<Map<String, Object>> findAll();

	public abstract Page<Map<String, Object>> findWithPagination(Pageable pageable);

	public abstract Page<Map<String, Object>> findLastPage(int limit);

	public abstract int insert(Admin adminUser);

	public abstract int update(Admin adminUser);

	public abstract int changePassword(String oldPassword, String newPassword);

	public abstract int delete(Long adminId);

	public abstract Map<String, Object> checkLogin(Admin admin);

	public abstract Map<String, Object> findAdminUser(Long id);

	public abstract Map<String, Object> findAdminUserByAdminCode(String adminCode);

	public abstract int resetPassword(Long id, String password);

	public abstract List<Map<String, Object>> getAllAdminPermission(Long adminId);

	public abstract void updateAdminPermissions(Long adminId, Set<Long> permissionCodes);

	public abstract List<Map<String, Object>> getAllGroup(Long userId);

	public abstract void updateAdminGroups(Long adminId, Set<Long> groupIds);

}