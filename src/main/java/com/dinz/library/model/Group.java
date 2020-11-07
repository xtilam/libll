package com.dinz.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.dinz.library.common.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "[group]")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", unique = true, nullable = true, updatable = false)
    private Long id;

    @NaturalId
    @Column(name = "group_code", updatable = false, unique = true, nullable = true)
    private String groupCode;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "create_date", updatable = false)
    private Date createDate;
    @Column(name = "modified_date")
    private Date modifiedDate;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<GroupPermission> groupPermissions;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "create_by", referencedColumnName = "admin_code", updatable = false)
    private Admin createBy;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "modified_by", referencedColumnName = "admin_code")
    private Admin modifiedBy;
    @Column(name = "delete_status")
    private Integer deleteStatus;
//
////    @OneToMany(mappedBy = "group")
////    private Set<GroupAdmin> groupAdmin; 

    public void setGroupCode(String groupCode) {
        if (null != groupCode && IRegexPatternCheckData.CODE.matcher(groupCode).find())
            this.groupCode = groupCode;
    }

    public void setName(String name) {
        if (null != name) {
            name = name.trim();
            this.name = name.isEmpty() ? null : name;
        }
    }

    public void setDescription(String description) {
        if (null != description) {
            description = description.trim();
            this.description = description.isEmpty() ? null : description;
        }
    }

    @PrePersist
    public void onPrePersist() {
        Date now = new Date();
        this.setId(Utils.getRandomId());
        this.setModifiedDate(now);
        this.setCreateDate(now);
    }

}
