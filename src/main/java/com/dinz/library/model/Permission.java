package com.dinz.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dinz.library.common.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", updatable = false, unique = true)
    private Long id;
    @PropertyName(name = "Mã chức năng")
    @Column(name = "permission_code", updatable = false, unique = true, nullable = false)
    private String permissionCode;
    @PropertyName(name = "Tên chức năng")
    @Column(name = "name", nullable = false)
    private String name;
    @PropertyName(name = "Mô tả")
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "create_date", updatable = false)
    private Date createDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "deleteStatus")
    private Integer deleteStatus;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "create_by", referencedColumnName = "admin_code", updatable = false)
    private Admin createBy;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "modified_by", referencedColumnName = "admin_code")
    private Admin modifiedBy;

    @OneToMany(mappedBy = "permission")
    private List<PermissionAdmin> permissionAdmins;
    @OneToMany(mappedBy = "permission")
    private List<GroupPermission> groupPermissions;

    public void setPermissionCode(String permissionCode) {
        if (null != permissionCode && IRegexPatternCheckData.CODE.matcher(permissionCode).find()) {
            this.permissionCode = permissionCode;
        }
    }

    public void setName(String name) {
        String nameFill = null;
        if (name != null) {
            nameFill = name.trim();
            if (nameFill.isEmpty()) {
                nameFill = null;
            }
        }
        this.name = nameFill;
    }

    public void setDescription(String description) {
        String descriptionFill = null;
        if (description != null) {
            descriptionFill = description.trim();
            if (descriptionFill.isEmpty()) {
                descriptionFill = null;
            }
        }
        this.description = descriptionFill;
    }

}
