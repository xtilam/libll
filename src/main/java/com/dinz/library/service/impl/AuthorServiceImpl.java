package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.model.Author;
import com.dinz.library.repository.AuthorRepository;
import com.dinz.library.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	AuthorRepository authorRepo;

	@Override
	public List<Author> findAuthorById(Long id) {
		return authorRepo.findAuthorById(id);
	}

	@Override
	public Page<Author> findAll(Pageable page) {
		return authorRepo.findAll(page);
	}

	@Override
	public List<Author> findAll() {
		return authorRepo.findAll();
	}

	@Override
	public Integer countAll() {
		return authorRepo.countAll();
	}

	@Override
	public Page<Author> findLastPage(int limit) {
		int totalRecords = this.authorRepo.countAll();
		if (totalRecords > 0) {
			int maxPage = (totalRecords / limit) + (totalRecords % limit == 0 ? 0 : 1);
			return this.authorRepo.findAll(PageRequest.of(maxPage - 1, limit));
		} else {
			List<Author> list = new ArrayList<>();
			return new PageImpl<>(list, PageRequest.of(0, limit), totalRecords);
		}
	}

}
