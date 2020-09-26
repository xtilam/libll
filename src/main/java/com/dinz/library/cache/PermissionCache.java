/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import com.dinz.library.model.Permission;
import com.dinz.library.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dinzenida
 */
@Component
public class PermissionCache {

    // request , id permission
    public final Map<RequestKey, Long> permissionCache;

    @Autowired
    HttpServletRequest request;

    @Autowired
    PermissionService permissionService;

    @PersistenceContext
    EntityManager em;

    public PermissionCache() {
        this.permissionCache = new HashMap<>();
    }

    public Long getPermissionId(String url, String method) {
        return this.permissionCache.get(new RequestKey(url, method));
    }

    public Long getPermissionId() {
        return this.permissionCache.get(new RequestKey(request));
    }

    public synchronized int addPermission(Permission permission) throws Exception {
        RequestKey requestKey = new RequestKey(permission.getApi(), permission.getMethod());
        if (this.permissionCache.get(requestKey) != null) {
            throw new Exception("Phân quyền đã tồn tại");
        }
        int resultInsert = permissionService.insert(permission);
        if (resultInsert > 0) {
            synchronized (this.permissionCache) {
                this.permissionCache.put(requestKey, permission.getId());
            }
        }
        return resultInsert;
    }

    public synchronized int deletePermission(Long id) {
        int deleteResult = this.permissionService.delete(id);
        if (deleteResult > 0) {
            for (Entry<RequestKey, Long> entry : this.permissionCache.entrySet()) {
                if (entry.getValue().equals(id)) {
                    this.permissionCache.remove(entry.getKey());
                    break;
                }
            }
        }
        return deleteResult;
    }

    public synchronized int updatePermission(Permission permission) throws Exception {
        RequestKey requestKey = new RequestKey(permission.getApi(), permission.getMethod());
        Long id = this.permissionCache.get(requestKey);
        if (id != null && !id.equals(permission.getId())) {
            throw new Exception("Phân quyền đã tồn tại");
        }

        int updateResult = this.permissionService.update(permission);
        if (updateResult > 0) {
            for (Entry<RequestKey, Long> entry : this.permissionCache.entrySet()) {
                if (entry.getValue().equals(permission.getId())) {
                    this.permissionCache.remove(entry.getKey());
                    this.permissionCache.put(requestKey, permission.getId());
                    break;
                }
            }
        }
        return updateResult;
    }

    public void updateAllPermission() {
        this.permissionCache.clear();
        this.em.createQuery("select p.id, p.api, p.method from Permission p where p.deleteStatus = 0", Object[].class).getResultList().forEach(o -> {
            this.permissionCache.put(new RequestKey(o[1].toString(), o[2].toString()), (Long) o[0]);
        });
        System.out.println(this.permissionCache.size());
    }
}
