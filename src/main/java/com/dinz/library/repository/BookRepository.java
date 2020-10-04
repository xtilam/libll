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
 *
 * @author DinzeniLL
 */
@Repository
public interface BookRepository extends org.springframework.data.repository.Repository<Book, Long> {

	@Query(value = "select b from Book b where b.deleteStatus = 0 and b.id = :id")
	Book findById(@Param("id") Long id);

	@Query(value = "select b from Book b where b.deleteStatus = 0")
	Page<Book> findBooks(Pageable page);

	@Query(value = "select b from Book b where b.deleteStatus = 0")
	List<Book> findBooks();

	@Query(value = "select count(b.id) from Book b where b.deleteStatus = 0")
	Integer countBook();

	@Modifying
	@Query(value = "insert into category_book(isbn, category_code) values(:isbn, :categoryCode)", nativeQuery = true)
	void insertCategoryBook(Long isbn, String categoryCode);

	@Modifying
	@Query(value = "insert into author_book(isbn, author_code) values(:isbn, :authorCode)", nativeQuery = true)
	void insertAuthorBook(Long isbn, String authorCode);
}
