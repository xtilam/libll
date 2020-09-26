package com.dinz.library.service;

import com.dinz.library.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DinzeniLL
 */
public interface BookService {
    public Page<Book> findBooks(Pageable page);
    public List<Book> findBooks();
    public Page<Book> findBooksLastPage(int limit);
}
