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

import com.dinz.library.model.Publisher;
import com.dinz.library.repository.PublisherRepository;
import com.dinz.library.service.PublisherService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * @author DinzeniLL
 */
@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    PublisherRepository publisherRepo;

    @PersistenceContext
    EntityManager em;

    @Override
    public List<Publisher> findAll() {
        return publisherRepo.findAll();
    }

    @Override
    public Page<Publisher> findAll(Pageable page) {
        return publisherRepo.findAll(page);
    }

    @Override
    public Page<Publisher> findLastPage(int limit) {
        return Utils.getLastPageService(
                this.publisherRepo::count,
                (_page, _limit) -> this.publisherRepo.findAll(PageRequest.of(_page, _limit)),
                limit
        );
    }

    @Transactional
    @Override
    public void insert(Publisher entity) {
        this.em.persist(entity);
    }

    @Transactional
    @Override
    public void update(Publisher entity) {
        this.em.merge(entity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.publisherRepo.deleteById(id);
    }

    @Override
    public Publisher findById(Long id) {
        return this.publisherRepo.findById(id);
    }

}
