package com.dinz.library.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission_admin")
public class PermissionAdmin implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "permission_code", referencedColumnName = "permission_code")
    private Permission permission;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "admin_code", referencedColumnName = "admin_code")
    private Admin admin;

    @Override
    public boolean equals(Object o){
        if(o instanceof GroupAdmin){
            PermissionAdmin pa = (PermissionAdmin) o;
            Long permissionId = null == this.permission ? null : this.permission.getId();
            Long gpPermissionId = null == pa.permission ? null : pa.permission.getId();
            Long adminId = null == this.admin ? null : this.admin.getId();
            Long gpAdminId = null == pa.admin ? null : pa.admin.getId();
            return (Objects.equals(permissionId, gpPermissionId) && Objects.equals(adminId, gpAdminId));
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        Long permissionId = null == this.permission ? null : this.permission.getId();
        Long adminId = null == this.admin ? null : this.admin.getId();
        return Objects.hash(permissionId, adminId);
    }
}