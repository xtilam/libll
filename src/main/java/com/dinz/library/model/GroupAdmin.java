/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.model;

import com.dinz.library.common.Utils;
import com.dinz.library.common.Utils;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author dinzenida
 */
@Entity
@Table(name = "group_admin")
@Data
public class GroupAdmin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_code", referencedColumnName = "group_code")
    private Group group;
    @ManyToOne
    @JoinColumn(name = "admin_code", referencedColumnName = "admin_code")
    private Admin admin;
    
    @Column(name = "admin_code", insertable = false, updatable = false)
    private String adminCode;

    @Column(name = "group_code", insertable = false, updatable = false)
    private String groupCode;

    public static GroupAdmin createNew(String groupCode, String adminCode){
        GroupAdmin ga = new GroupAdmin();
        ga.id = Utils.getRandomId();
        ga.groupCode = groupCode;
        ga.adminCode = adminCode;
        return ga;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GroupAdmin) {
            GroupAdmin gp = (GroupAdmin) o;
            Long groupId = null == this.group ? null : this.group.getId();
            Long gpGroupId = null == gp.group ? null : gp.group.getId();
            Long adminId = null == this.admin ? null : this.admin.getId();
            Long gpAdminId = null == gp.admin ? null : gp.admin.getId();
            return (Objects.equals(groupId, gpGroupId) && Objects.equals(adminId, gpAdminId));
        }
        return false;
    }

    @Override
    public int hashCode() {
        Long groupId = null == this.group ? null : this.group.getId();
        Long adminId = null == this.admin ? null : this.admin.getId();
        return Objects.hash(groupId, adminId);
    }

}
