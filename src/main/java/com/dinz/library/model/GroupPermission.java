package com.dinz.library.model;

import com.dinz.library.common.Utils;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.dinz.library.common.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[group_permission]")
public class GroupPermission implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_code", referencedColumnName = "group_code")
    private Group group;
    @ManyToOne
    @JoinColumn(name = "permission_code", referencedColumnName = "permission_code")
    private Permission permission;

    @PrePersist
    public void onPrePersist() {
        this.id = Utils.getRandomId();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GroupPermission) {
            GroupPermission gp = (GroupPermission) object;
            Long gpPermissionId = null == gp.getPermission() ? null : gp.getPermission().getId();
            Long permissionId = null == this.getPermission() ? null : this.getPermission().getId();
            Long gpGroupId = null == gp.getGroup() ? null : gp.getGroup().getId();
            Long groupId = null == this.getGroup() ? null : this.getGroup().getId();

            boolean isPermisionIdEquals = gpPermissionId == null ? permissionId == null : gpPermissionId.equals(permissionId);
            boolean isGroupIdEquals = gpGroupId == null ? groupId == null : gpGroupId.equals(groupId);
            
            if (isPermisionIdEquals && isGroupIdEquals) return true;
            return gpPermissionId == null;
        }
        return false;
    }

    @Override
    public int hashCode() {
        Long permissionId = null == this.getPermission() ? null : this.getPermission().getId();
        Long groupId = null == this.getGroup() ? null : this.getGroup().getId();
        return Objects.hash(groupId, permissionId);
    }
}