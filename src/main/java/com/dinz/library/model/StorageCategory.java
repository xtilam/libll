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

@Entity
@Table(name = "storage_category")
public class StorageCategory implements Serializable {
    private static final long serialVersionUID = -5158041261998669181L;
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "storage_category_code")
    private String storageCategoryCode;
    @Column(name = "storage_category")
    private String storageCategory;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "storageCategory")
    private List<Storage> storages;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "create_by", referencedColumnName = "admin_code")
    private Admin createBy;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "modified_by", referencedColumnName = "admin_code")
    private Admin modifiedBy;

    // #region constructor
    public StorageCategory() {
    }

    // #endregion
    // #region getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageCategoryCode() {
        return storageCategoryCode;
    }

    public void setStorageCategoryCode(String storageCategoryCode) {
        this.storageCategoryCode = storageCategoryCode;
    }

    public String getStorageCategory() {
        return storageCategory;
    }

    public void setStorageCategory(String storageCategory) {
        this.storageCategory = storageCategory;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Admin getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Admin createBy) {
        this.createBy = createBy;
    }

    public Admin getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Admin modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // #endregion

}
