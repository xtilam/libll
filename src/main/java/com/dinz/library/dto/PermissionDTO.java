package com.dinz.library.dto;

import com.dinz.library.model.Permission;

public class PermissionDTO {
    private Long id;
    private String permissionCode;
    private String description;
    private String api;
    private String name;
    private String method;

    public Permission convert() {
        Permission permission = new Permission();
        permission.setPermissionCode(this.permissionCode);
        permission.setName(this.getName());
        permission.setApi(this.getApi());
        permission.setDescription(this.getDescription());
        permission.setMethod(this.getMethod());
        permission.setId(this.id);
        return permission;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}