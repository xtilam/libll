/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.dinz.library.LibApplication;
import com.dinz.library.common.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 *
 * @author DinzeniLL
 */
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "create_date", updatable = false)
    private Date createDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @JsonIgnore
    @Column(name = "delete_status", updatable = false)
    private Integer deleteStatus = 0;
    @Column(name = "create_by", updatable = false)
    private String createBy;
    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BaseEntity ? Objects.equals(this.id, ((BaseEntity) obj).id) : false;
    }

    @Override
    public int hashCode() {
        return this.id instanceof Long ? this.id.hashCode() : 0;
    }

    @PrePersist
    public void onPersist() {
        this.id = Utils.getRandomId();
        String create = LibApplication.getAdminContext().getAdminCode();
        Date now = new Date();

        this.createDate = now;
        this.modifiedDate = now;

        this.createBy = create;
        this.modifiedBy = create;
    }

    @PreUpdate
    public void onUpdate() {
        String modified = LibApplication.getAdminContext().getAdminCode();
        Date now = new Date();

        this.modifiedBy = modified;
        this.modifiedDate = now;
    }
}
