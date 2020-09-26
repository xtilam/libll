package com.dinz.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dinz.library.model.Author;

public interface AuthorService {

	Integer countAll();

	List<Author> findAll();

	Page<Author> findAll(Pageable page);

	List<Author> findAuthorById(Long id);

	Page<Author> findLastPage(int limit);
}
