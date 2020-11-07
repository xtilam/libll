/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.dinz.library.LibApplication;
import com.dinz.library.common.Utils;
import com.dinz.library.constants.SystemConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author dinzenida
 */
public class AdminLoginSession implements IAdminLoginSession {

    @JsonProperty
    private Set<String> permissions;
    private Set<String> groups;

    @JsonProperty
    private Map<Long, AdminLoginInfo> tokens = new HashMap<>();

    public AdminLoginSession() {
        tokens = new HashMap<>();
    }

    private AdminUserLoginCache getAdminLoginCache() {
        return LibApplication.getContext().getBean(AdminUserLoginCache.class);
    }

    /**
     * Kiểm tra quyền của người dùng
     *
     * @param permission Tên chức năng
     * @param adminCache Đối tượng lưu trữ thông tin đăng nhập
     * @return
     */
    @Override
    public boolean checkPermission(String permission, Long userId) {
        AdminUserLoginCache adminCache = this.getAdminLoginCache();
        this.updateAllGroupAndPermission(adminCache, userId);
        for (Iterator<String> iterator = this.permissions.iterator(); iterator.hasNext();) {
            if (iterator.next().equals(permission)) {
                return true;
            }
        }

        Set<String> groupHavePermissions = adminCache.permissionGroup.get(permission);
        if (groupHavePermissions != null) {
            if (groupHavePermissions.stream().anyMatch(groupCode -> (this.groups.contains(groupCode)))) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getAllPermission(Long userId) {
        AdminUserLoginCache adminCache = this.getAdminLoginCache();
        this.updateAllGroupAndPermission(adminCache, userId);
        Set<String> pers = new HashSet<>();

        // Lọc qua chức năng
        this.permissions.forEach(per -> {
            pers.add(per);
        });

        // Lọc qua nhóm
        for (Entry<String, Set<String>> entry : adminCache.permissionGroup.entrySet()) {
            String per = entry.getKey();
            if (pers.contains(per)) {
                break;
            } else {
                for (String group : this.groups) {
                    if (entry.getValue().contains(group)) {
                        pers.add(per);
                        break;
                    }
                    ;
                }
            }
        }
        return pers;
    }

    private void updateAllGroupAndPermission(AdminUserLoginCache adminCache, Long userId) {
        // Tìm các chức năng của người dùng
        if (this.permissions == null) {
            List<Map<String, Object>> lsPermission = adminCache.adminService.getAllAdminPermission(userId);
            this.permissions = new HashSet<>();
            lsPermission.forEach(map -> {
                if (Boolean.TRUE.equals(map.get("isAllow"))) {
                    this.permissions.add((String) map.get("code"));
                }
            });
        }

        // Tìm các nhóm chức năng của người dùng
        if (this.groups == null) {
            List<Map<String, Object>> lsGroup = adminCache.adminService.getAllGroup(userId);
            this.groups = new HashSet<>();
            lsGroup.forEach(map -> {
                this.groups.add((String) map.get("groupCode"));
            });
        }
    }

    @Override
    public synchronized Long addToken(AdminLoginInfo adminLoginInfo) {
        Long tokenId = Utils.getRandomId();
        this.tokens.put(tokenId, adminLoginInfo);
        return tokenId;
    }

    @Override
    public synchronized void removeToken(Long tokenId) {
        tokens.remove(tokenId);
        if (this.tokens.isEmpty()) {
            this.clearGroups();
            this.clearPermissions();
        }
    }

    @Override
    public synchronized void clearPermissions() {
        this.permissions = null;
    }

    @Override
    public synchronized void clearGroups() {
        this.groups = null;
    }

    @Override
    public synchronized void clearCache(Long userId, AdminUserLoginCache adminCache) {
        Long lastUsed = 0L;
        Long now = System.currentTimeMillis();
        for (Entry<Long, AdminLoginInfo> entry : this.tokens.entrySet()) {
            Long lastLogin = entry.getValue().getLastUsed();
            if (lastLogin + SystemConstants.MAX_TIME_LIFE_ADMIN_TOKEN < now) {
                this.tokens.remove(entry.getKey());
            } else if (lastLogin > lastUsed) {
                lastUsed = lastLogin;
            }
        }
        if (this.tokens.isEmpty()) {

        }
    }

    @Override
    public synchronized boolean updateToken(Long tokenId, HttpServletRequest req, Long adminId,
            AdminUserLoginCache adminCache) {
        AdminLoginInfo loginInfo = null;
        loginInfo = this.tokens.get(tokenId);
        Long timeNow = System.currentTimeMillis();

        if (null == loginInfo) {
            return false;
        } else if (loginInfo.getLastUsed() + SystemConstants.MAX_TIME_LIFE_ADMIN_TOKEN < timeNow) {
            this.removeToken(tokenId);
            return false;
        } else {
            loginInfo.updateToken(req);
            return true;
        }
    }

    Map<Long, AdminLoginInfo> getTokens() {
        return tokens;
    }
}
