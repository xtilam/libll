/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service;

import com.dinz.library.model.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author DinzeniLL
 */
public interface CategoryService {

    public List<Category> findCategories();

    public Page<Category> findCategories(Pageable page);
    
    public Page<Category> findLastPage(int limit);

	Category findById(Long id);
}
