package com.dinz.library.repository;

import com.dinz.library.model.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DinzeniLL
 */
@Repository
public interface CategoryRepository extends org.springframework.data.repository.Repository<Category, Long> {

    @Query("select c from Category c where c.deleteStatus = 0")
    public List<Category> findCategories();

    @Query("select c from Category c where c.deleteStatus = 0")
    public Page<Category> findCategories(Pageable page);

    @Query(value = "select count(c.id) from Category c where c.deleteStatus = 0") 
    public Integer countAll();
}
