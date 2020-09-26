/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import com.dinz.library.model.Admin;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dinz.library.service.AdminService;
import java.util.Map.Entry;

/**
 * Lưu trữ tất cả các token của người dùng đã đăng nhập vào hệ thống
 *
 * @author dinzenida
 */
@Component
public class AdminUserLoginCache {

    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    AdminService adminService;

    // userid , thông tin các token đang được sử dụng
    final Map<Long, IAdminLoginSession> adminLoginCache = new HashMap<>();

    public AdminUserLoginCache() {
        AdminLoginSession adminLoginSession = new AdminLoginSession();
        AdminLoginInfo adminLoginInfo = new AdminLoginInfo("-", "-");
        adminLoginInfo.setLastUsed(System.currentTimeMillis());
        adminLoginCache.put(571747939356005L, adminLoginSession);
        adminLoginSession.getTokens().put(3092678241171455L, adminLoginInfo);
    }

    public void clearAllPermission() {
        synchronized (this.adminLoginCache) {
            this.adminLoginCache.forEach((id, iASession) -> {
                if (iASession instanceof AdminLoginSession) {
                    ((AdminLoginSession) iASession).clearPermission();
                }
            });
        }
    }

    public Map<Long, IAdminLoginSession> getCacheMap() {
        return adminLoginCache;
    }

    public int deleteUser(Long userId) {
        int removeResult = this.adminService.delete(userId);
        if (removeResult > 0) {
            synchronized (this.adminLoginCache) {
                IDeathAdmin death = new IDeathAdmin() {
                };
                this.adminLoginCache.put(userId, death);
            }
        }
        return removeResult;
    }

    /**
     * Kiểm tra token có hợp lệ hay không và nếu hợp lệ thì lưu lại thông tin về
     * phiên sử dụng lần cuối của token đó
     *
     * @return
     */
    public boolean checkTokenValidAndUpdateIt() {
        Long adminId = (Long) session.getAttribute("adminId");
        Long tokenId = (Long) session.getAttribute("tokenId");
        IAdminLoginSession iASession = this.adminLoginCache.get(adminId);

        if (iASession instanceof AdminLoginSession) {
            return ((AdminLoginSession) iASession).updateToken(tokenId, this.httpServletRequest);
        }
        return false;
    }

    public void clearPermission(Long userId) {
        synchronized (this.adminLoginCache) {
            IAdminLoginSession iASession = this.adminLoginCache.get(userId);
            if (iASession instanceof AdminLoginSession) {
                ((AdminLoginSession) iASession).clearPermission();
            }
        }
    }

    public Set<Long> getAdminUsersId() {
        return this.adminLoginCache.keySet();
    }

    public Set<Long> getPermissions() {
        AdminLoginSession aSession = (AdminLoginSession) this.session.getAttribute("adminSession");
        if (aSession != null) {
            return aSession.getPermissions(this.session.getAttribute("adminCode").toString(), this);
        }
        return null;
    }

    public boolean updateAdminPermissions(Long userId, Set<Long> permissions) {
        try {
            this.adminService.updateAdminPermissions(userId, permissions);
            IAdminLoginSession iASession = this.adminLoginCache.get(userId);
            if (iASession instanceof AdminLoginSession) {
                ((AdminLoginSession) iASession).clearPermission();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeToken() {
        ((AdminLoginSession) this.session.getAttribute("adminSession"))//
                .removeToken((Long) this.session.getAttribute("tokenId"));
    }

    public boolean adminUserLogin() {
        Long userId = (Long) session.getAttribute("adminId");
        AdminLoginSession adminSession;

        synchronized (this.adminLoginCache) {
            IAdminLoginSession iASession = this.adminLoginCache.get(userId);
            if (iASession == null) {
                adminSession = new AdminLoginSession();
                this.adminLoginCache.put(userId, adminSession);
            } else if (!(iASession instanceof IDeathAdmin)) {
                adminSession = (AdminLoginSession) iASession;
            } else {
                return false;
            }
        }

        Long token = adminSession.addToken(new AdminLoginInfo(this.httpServletRequest));
        this.session.setAttribute("tokenId", token);
        return true;
    }
}
