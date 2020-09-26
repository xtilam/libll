/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dinz.library.common.Utils;
import com.dinz.library.constants.SystemConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dinzenida
 */
public class AdminLoginSession implements IAdminLoginSession {

    @JsonProperty
    private Set<Long> permissions;
    @JsonProperty
    private Map<Long, AdminLoginInfo> tokens = new HashMap<>();

    public AdminLoginSession() {
        tokens = new HashMap<>();
    }

    public synchronized Set<Long> getPermissions(String adminCode, AdminUserLoginCache adminCache) {
        if (this.permissions == null) {
            this.permissions = adminCache.adminService.getAllPermission(adminCode);
        }
        return this.permissions;
    }

    public synchronized Long addToken(AdminLoginInfo adminLoginInfo) {
        Long id = Utils.getRandomId();
        tokens.put(id, adminLoginInfo);
        return id;
    }

    public synchronized void removeToken(Long tokenId) {
        tokens.remove(tokenId);
        if (this.tokens.size() > 0) {
            this.clearPermission();
        }
    }

    public synchronized void clearPermission() {
        this.permissions = null;
    }

    public synchronized void clearCache(Long userId) {
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
        if (this.tokens.isEmpty() || lastUsed + SystemConstants.MAX_TIME_LIFE_PERMISSIONS_IN_ADMIN_CACHE < now) {
            this.permissions = null;
        }
    }

    public synchronized int removeUser(Long id, AdminUserLoginCache cache) {
        int removeResult = cache.adminService.delete(id);
        if (removeResult > 0) {
            synchronized (cache.adminLoginCache) {
                IDeathAdmin death = new IDeathAdmin() {
                };
                cache.adminLoginCache.put(id, death);
            }
        }
        return removeResult;
    }

    public synchronized boolean updateToken(Long tokenId, HttpServletRequest req) {
        AdminLoginInfo loginInfo = null;
        loginInfo = this.tokens.get(tokenId);
        Long timeNow = System.currentTimeMillis();
        
        if (null == loginInfo) {
            return false;
        } else if (loginInfo.getLastUsed() + SystemConstants.MAX_TIME_LIFE_ADMIN_TOKEN < timeNow) {
            this.removeToken(tokenId);
            return false;
        } else {
            AdminLoginInfo adminLoginInfoUpdate = new AdminLoginInfo(req);
            adminLoginInfoUpdate.setLastUsed(timeNow);
            req.getSession().setAttribute("adminSession", this);
            return true;
        }
    }

    Map<Long, AdminLoginInfo> getTokens() {
        return tokens;
    }
}
