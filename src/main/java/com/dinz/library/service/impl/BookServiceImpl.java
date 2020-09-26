/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import com.dinz.library.model.Book;
import com.dinz.library.repository.BookRepository;
import com.dinz.library.service.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author DinzeniLL
 */

@Service
public class BookServiceImpl implements BookService {
    
    @Autowired
    BookRepository bookRepo;
    
    @PersistenceContext
    EntityManager em;

    @Override
    public Page<Book> findBooks(Pageable page) {
        return this.bookRepo.findBooks(page);
    }
    
    @Override
    public List<Book> findBooks() {
        return this.bookRepo.findBooks();
    }
    
    @Override
    public Page<Book> findBooksLastPage(int limit) {
        int totalRecords = this.bookRepo.countBook();
        if (totalRecords > 0) {
            int maxPage = (totalRecords / limit) + (totalRecords % limit == 0 ? 0 : 1);
            return this.bookRepo.findBooks(PageRequest.of(maxPage - 1, limit));
        } else {
            List<Book> list = new ArrayList<>();
            return new PageImpl<>(list, PageRequest.of(0, limit), totalRecords);
        }
    }
}
