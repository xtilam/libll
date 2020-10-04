/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.model.Category;
import com.dinz.library.repository.CategoryRepository;
import com.dinz.library.service.CategoryService;

/**
 *
 * @author DinzeniLL
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	@Override
	public List<Category> findCategories() {
		return this.categoryRepo.findCategories();
	}

	@Override
	public Page<Category> findCategories(Pageable page) {
		return this.categoryRepo.findCategories(page);
	}

	@Override
	public Page<Category> findLastPage(int limit) {
		int totalRecords = this.categoryRepo.countAll();
		if (totalRecords > 0) {
			int maxPage = (totalRecords / limit) + (totalRecords % limit == 0 ? 0 : 1);
			return this.categoryRepo.findCategories(PageRequest.of(maxPage - 1, limit));
		} else {
			List<Category> list = new ArrayList<>();
			return new PageImpl<>(list, PageRequest.of(0, limit), totalRecords);
		}
	}

	@Override
	public Category findById(Long id) {
		return this.categoryRepo.findById(id);
	}
}
