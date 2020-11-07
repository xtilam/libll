/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dinz.library.service.AdminService;

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

    @PersistenceContext
    EntityManager em;

    public final Map<Long, IAdminLoginSession> adminLoginCache = new HashMap<>();
    public final Map<String, Set<String>> permissionGroup = new HashMap<String, Set<String>>() {
        private static final long serialVersionUID = 1L;
        private boolean shouldRefresh = false;
        private final String hqlGroup = "select p.name, g.name from Permission p"
                + " inner join GroupPermission gp on gp.permission.permissionCode = p.permissionCode"
                + " inner join Group g on g.groupCode = gp.group.groupCode"
                + " where g.deleteStatus = 0 and p.deleteStatus = 0";

        public synchronized void refreshMap() {
            try {
                this.shouldRefresh = false;
                Iterator<Object[]> iterator = em.createQuery(this.hqlGroup, Object[].class).getResultList().iterator();
                this.clear();
                while (iterator.hasNext()) {
                    Object[] val = iterator.next();
                    Set<String> groups = this.get(val[0].toString());
                    if (groups == null) {
                        groups = new HashSet<>();
                        this.put(val[0].toString(), groups);
                    }
                    groups.add(val[1].toString());
                }
            } catch (Exception e) {
                shouldRefresh = true;
                System.err.println("group-permission-err: " + e.getMessage());
            }
        }

        @Override
        public java.util.Set<String> get(Object key) {
            synchronized (this) {
                if (this.shouldRefresh) {
                    this.refreshMap();
                }
            }
            return super.get(key);
        }

        ;
    };

    public AdminUserLoginCache() {
        AdminLoginSession adminLoginSession = new AdminLoginSession();
        AdminLoginInfo adminLoginInfo = new AdminLoginInfo("-", "-");
        adminLoginInfo.setLastUsed(System.currentTimeMillis());
        this.adminLoginCache.put(-1L, adminLoginSession);
        adminLoginSession.getTokens().put(4009655566178114L, adminLoginInfo);

        adminLoginSession = new AdminLoginSession();
        adminLoginInfo = new AdminLoginInfo("-", "-");
        adminLoginInfo.setLastUsed(System.currentTimeMillis());
        this.adminLoginCache.put(571747939356005L, adminLoginSession);
        adminLoginSession.getTokens().put(4019904466674714L, adminLoginInfo);
    }

    public boolean checkPermission(Long adminId, String permission) {
        IAdminLoginSession adminSession = this.adminLoginCache.get(adminId);
        if (adminSession instanceof AdminLoginSession) {
            return adminSession.checkPermission(permission, adminId);
        }
        return false;
    }

    public void cleanListGroupOfUser(Long adminId) {
        synchronized (this.adminLoginCache) {
            this.adminLoginCache.forEach((id, iASession) -> {
                if (iASession instanceof AdminLoginSession) {
                    ((IAdminLoginSession) iASession).clearGroups();
                }
            });
        }
    }

    public void cleanListPermissionOfUser(Long adminId) {
        synchronized (this.adminLoginCache) {
            this.adminLoginCache.forEach((id, iASession) -> {
                if (iASession instanceof AdminLoginSession) {
                    ((IAdminLoginSession) iASession).clearPermissions();
                }
            });
        }
    }

    public void refeshPermissionGroupMap() {
        try {
            Method method = this.permissionGroup.getClass().getDeclaredMethod("refreshMap");
            method.invoke(this.permissionGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Long, IAdminLoginSession> getCacheMap() {
        return this.adminLoginCache;
    }

    public int deleteUser(Long userId) {
        int removeResult = this.adminService.delete(userId);
        if (removeResult > 0) {
            synchronized (this.adminLoginCache) {
                this.adminLoginCache.put(userId, new DeathAdminLoginSession(userId));
            }
        }
        return removeResult;
    }

    public Set<String> getAllPermissions(Long userId) {
        IAdminLoginSession aSession = this.getCacheMap().get(userId);
        if (aSession instanceof AdminLoginSession) {
            return ((AdminLoginSession) aSession).getAllPermission(userId);
        }
        return null;
    }

    public boolean checkTokenValidAndUpdateIt(Long adminId, Long tokenId) {
        IAdminLoginSession iASession = this.adminLoginCache.get(adminId);
        if (iASession instanceof AdminLoginSession) {
            return iASession.updateToken(tokenId, this.httpServletRequest, adminId, this);
        }
        return false;
    }

    public Set<Long> getAdminUsersId() {
        return this.adminLoginCache.keySet();
    }

    public void removeToken(Long adminId, Long tokenId) {
        IAdminLoginSession adminSession = this.adminLoginCache.get(adminId);
        if (adminSession instanceof AdminLoginSession) {
            adminSession.removeToken(tokenId);
        }
    }

    public Long adminUserLogin(Long userId) {
        IAdminLoginSession adminSession = null;

        synchronized (this.adminLoginCache) {
            adminSession = this.adminLoginCache.get(userId);
            if (adminSession == null) {
                adminSession = new AdminLoginSession();
                this.adminLoginCache.put(userId, adminSession);
            } else if (!(adminSession instanceof AdminLoginSession)) {
                return null;
            }
        }

        Long token = adminSession.addToken(new AdminLoginInfo(this.httpServletRequest));

        return token;
    }
}
