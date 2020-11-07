/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import java.util.List;

import com.dinz.library.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.model.Category;
import com.dinz.library.repository.CategoryRepository;
import com.dinz.library.service.CategoryService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * @author DinzeniLL
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepo;

    @PersistenceContext
    EntityManager em;
    @Override
    public List<Category> findAll() {
        return this.categoryRepo.findAll();
    }

    @Override
    public Page<Category> findAll(Pageable page) {
        return this.categoryRepo.findAll(page);
    }

    @Override
    public Page<Category> findLastPage(int limit) {
        return Utils.getLastPageService(
                this.categoryRepo::countAll,
                (_page, _limit) -> this.categoryRepo.findAll(PageRequest.of(_page, _limit)),
                limit);
    }

    @Transactional
    @Override
    public void insert(Category entity) {
        this.em.persist(entity);
    }

    @Transactional
    @Override
    public void update(Category entity) {
        this.em.merge(entity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.categoryRepo.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepo.findById(id);
    }
}
