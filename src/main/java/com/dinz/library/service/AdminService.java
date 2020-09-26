package com.dinz.library.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dinz.library.model.Admin;

public interface AdminService {
    public List<Map<String, Object>> findAll();

    public Page<Map<String, Object>> findWithPagination(Pageable pageable);

    public Page<Map<String, Object>> findLastPage(int limit);

    public int insert(Admin adminUser);

    public int update(Admin adminUser);

    public int changePassword(String oldPassword, String newPassword);

    public int delete(Long adminId);

    public Map<String, Object> checkLogin(Admin admin);
    
    public Map<String, Object> findAdminUser(Long id);
    
    public Map<String, Object> findAdminUserByAdminCode(String adminCode);

    public int resetPassword(Long id, String password);
    
    public Set<Long> getAllPermission(String adminCode);

    public List<Map<String, Object>> getAllAdminPermission(Long adminId);
    
    public void updateAdminPermissions(Long adminId, Set<Long> permissionCodes);
    
    public List<Map<String, Object>> getAllGroup(String adminCode);
    
    public void updateAdminGroups(Long adminId, Set<Long> groupIds);
}