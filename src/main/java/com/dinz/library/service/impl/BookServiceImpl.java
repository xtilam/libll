/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.dinz.library.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.common.updatemtm.UpdateMTM;
import com.dinz.library.model.Author;
import com.dinz.library.model.Book;
import com.dinz.library.model.Category;
import com.dinz.library.repository.BookRepository;
import com.dinz.library.service.BookService;
import com.dinz.library.service.PublisherService;

/**
 * @author DinzeniLL
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UpdateMTM updateMTM;

    @PersistenceContext
    EntityManager em;

    @Autowired
    HttpSession session;

    @Autowired
    PublisherService publisherService;

    @Override
    public Page<Book> findAll(Pageable page) {
        return this.bookRepo.findAll(page);
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepo.findAll();
    }

    @Override
    public Page<Book> findLastPage(int limit) {
        return Utils.getLastPageService(this.bookRepo::count,
                (_page, _limit) -> this.bookRepo.findAll(PageRequest.of(_page, _limit)),
                limit);
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepo.findById(id);
    }

    @Transactional
    @Override
    public void insert(Book book) {
        book.setPublisher(this.publisherService.findById(book.getPublisher().getId()));
        updateMTM.insert(Book.class, Category.class, book);
        updateMTM.insert(Book.class, Author.class, book);
        this.em.persist(book);
    }

    @Transactional
    @Override
    public void update(Book book) {
        book.setPublisher(this.publisherService.findById(book.getPublisher().getId()));
        updateMTM.update(Book.class, Category.class, book);
        updateMTM.update(Book.class, Author.class, book);
        this.em.merge(book);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.bookRepo.deleteById(id);
    }
}
