package com.dinz.library.dto;

import com.dinz.library.model.StorageCategory;

public class StorageCategoryDTO {
    public Long id;
    public String storageCategoryCode;
    public String storageCategory;
    public String description;

    public StorageCategory toStorageCategory(){
        StorageCategory sc = new StorageCategory();
        sc.setId(this.getId());
        sc.setStorageCategoryCode(this.getStorageCategoryCode());
        sc.setStorageCategory(this.getStorageCategory());
        sc.setDescription(this.getDescription());
        return sc;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // #endregion
}