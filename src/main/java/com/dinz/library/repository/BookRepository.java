/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dinz.library.model.Book;

/**
 * @author DinzeniLL
 */
@Repository
public interface BookRepository extends org.springframework.data.repository.Repository<Book, Long> {

    @Query(value = "select b from Book b where b.deleteStatus = 0 and b.id = :id")
    Book findById(@Param("id") Long id);

    @Query(value = "select b from Book b where b.deleteStatus = 0")
    Page<Book> findAll(Pageable page);

    @Query(value = "select b from Book b where b.deleteStatus = 0")
    List<Book> findAll();

    @Query(value = "select count(b.id) from Book b where b.deleteStatus = 0")
    Integer count();

    @Modifying
    @Query(value = "update Book b set b.deleteStatus = 0 where b.id = :idBook")
    int deleteById(@Param("idBook") Long idBook);
}
