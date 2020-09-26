package com.dinz.library.repository;

import java.util.Map;


import com.dinz.library.model.Permission;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface PermissionRepository extends Repository<Permission, Long> {
    public static class ecs{

    }
    static class QueryString {
        static final String DELETE_PERMISSION = "update Permission p set"//
                + " p.deleteStatus = 1"//
                + ",p.modifiedBy.adminCode = ?#{#permission.get('modifiedBy')}"//
                + ",p.modifiedDate = ?#{new java.util.Date()}"//
                + " where p.id = ?#{#permission.get('id')}";
    }

    @Modifying
    @Query(value = QueryString.DELETE_PERMISSION)
    public int deletePermission(@Param("permission") Map<String, Object> permission);
}