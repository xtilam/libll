package com.dinz.library.model;

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
@Table(name = "storage")
public class Storage {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "storage_code")
    private String storageCode;
    @Column(name = "description")
    private String description;
    @Column(name = "parent_storage")
    private String parentStorageCode;
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

    @OneToMany(mappedBy = "storage")
    private List<BookDetail> bookDetails;

    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "storage_category_code", referencedColumnName = "storage_category_code")
    private StorageCategory storageCategory;

    // #region constructor
    public Storage() {
    }
    // #endregion

    // #region getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentStorageCode() {
        return parentStorageCode;
    }

    public void setParentStorageCode(String parentStorageCode) {
        this.parentStorageCode = parentStorageCode;
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

    public List<BookDetail> getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(List<BookDetail> bookDetails) {
        this.bookDetails = bookDetails;
    }

    public StorageCategory getStorageCategory() {
        return storageCategory;
    }

    public void setStorageCategory(StorageCategory storageCategory) {
        this.storageCategory = storageCategory;
    }
    // #endregion

}