/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.common.Utils;
import com.dinz.library.model.Author;
import com.dinz.library.model.Book;
import com.dinz.library.model.Category;
import com.dinz.library.repository.BookRepository;
import com.dinz.library.service.AuthorService;
import com.dinz.library.service.BookService;
import com.dinz.library.service.CategoryService;
import com.dinz.library.service.PublisherService;

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

	@Autowired
	HttpSession session;

	@Autowired
	CategoryService categoryService;

	@Autowired
	AuthorService authorService;

	@Autowired
	PublisherService publisherService;

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

	@Override
	public Book findById(Long id) {
		return this.bookRepo.findById(id);
	}

	@Transactional
	@Override
	public void insert(Book book) {

		Date now = new Date();
		String admin = this.session.getAttribute("adminCode").toString();
		book.setId(Utils.getRandomId());
		book.setCreateDate(now);
		book.setModifiedDate(now);
		book.setModifiedBy(admin);
		book.setCreateBy(admin);
		book.setQuantity(0);
		book.setRate(0L);
		book.setRateCount(0);
		book.setDeleteStatus(0);

		if (book.getPublisher() != null) {
			book.setPublisher(this.publisherService.findById(book.getPublisher().getId()));
		} else {
			throw new RuntimeException("Tác giả không được trống");
		}

		if (book.getCategories() != null && book.getCategories().size() != 0) {
			book.getCategories().stream().forEach((category) -> {
				Category c = this.categoryService.findById(category.getId());
				if (null != c) {
					this.bookRepo.insertCategoryBook(book.getIsbn(), c.getCategoryCode());
				} else {
					throw new RuntimeException("Mã thể loại không tồn tại: " + category.getId());
				}

			});
		} else {
			throw new RuntimeException("Mã thể loại không được trống");
		}

		if (book.getAuthors() != null && book.getAuthors().size() != 0) {
			book.getAuthors().stream().forEach((author) -> {
				Author a = this.authorService.findAuthorById(author.getId());
				if (null != a) {
					this.bookRepo.insertAuthorBook(book.getIsbn(), a.getAuthorCode());
				} else {
					throw new RuntimeException("Tác giả không tồn tại: " + author.getId());
				}
			});
		} else {
			throw new RuntimeException("Tác giả không được trống");
		}

		this.em.persist(book);
	}

	@Override
	public int update(Book book) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	@Override
	public int delete(Book book) {
//		if(null != book.getCategories()) {
//			for(Category category: book.getCategories()) {
//				this.em.find(Category.class, category.getId());
//			}
//		}
		return 1;
	}

}
