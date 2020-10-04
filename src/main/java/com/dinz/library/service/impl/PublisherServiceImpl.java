/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.model.Publisher;
import com.dinz.library.repository.PublisherRepository;
import com.dinz.library.service.PublisherService;

/**
 *
 * @author DinzeniLL
 */
@Service
public class PublisherServiceImpl implements PublisherService {
	@Autowired
	PublisherRepository publisherRepo;

	@Override
	public List<Publisher> getPublishers() {
		return publisherRepo.getPublishers();
	}

	@Override
	public Page<Publisher> getPublishers(Pageable page) {
		return publisherRepo.getPublishers(page);
	}

	@Override
	public Page<Publisher> findLastPage(int limit) {
		int totalRecords = this.publisherRepo.countAll();
		if (totalRecords > 0) {
			int maxPage = (totalRecords / limit) + (totalRecords % limit == 0 ? 0 : 1);
			return this.publisherRepo.getPublishers(PageRequest.of(maxPage - 1, limit));
		} else {
			List<Publisher> list = new ArrayList<>();
			return new PageImpl<>(list, PageRequest.of(0, limit), totalRecords);
		}
	}

	@Override
	public Publisher findById(Long id) {
		return this.publisherRepo.findById(id);
	}

}
