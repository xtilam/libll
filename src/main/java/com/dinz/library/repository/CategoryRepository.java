package com.dinz.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dinz.library.model.Category;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author DinzeniLL
 */
@Repository
public interface CategoryRepository extends org.springframework.data.repository.Repository<Category, Long> {

    @Query("select c from Category c where c.deleteStatus = 0")
    public List<Category> findAll();

    @Query("select c from Category c where c.deleteStatus = 0")
    public Page<Category> findAll(Pageable page);

    @Query(value = "select c from Category c where c.deleteStatus = 0 and c.id = :id")
    public Category findById(@Param("id") Long id);

    @Query(value = "select count(c.id) from Category c where c.deleteStatus = 0")
    public Integer countAll();

    @Query(value = "update Category c set c.deleteStatus = 1 where c.id = :id")
    void deleteById(@Param("id") Long id);
}
