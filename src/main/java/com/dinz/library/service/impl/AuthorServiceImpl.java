package com.dinz.library.service.impl;

import java.util.List;

import com.dinz.library.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dinz.library.model.Author;
import com.dinz.library.repository.AuthorRepository;
import com.dinz.library.service.AuthorService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    AuthorRepository authorRepo;

    @PersistenceContext
    EntityManager em;

    @Override
    public Author findById(Long id) {
        return authorRepo.findById(id);
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
    public Page<Author> findLastPage(int limit) {
        return Utils.getLastPageService(
                this.authorRepo::countAll,
                (_page, _limit) -> this.authorRepo.findAll(PageRequest.of(_page, _limit)),
                limit
        );
    }

    @Transactional
    @Override
    public void insert(Author author) {
        this.em.persist(author);
    }

    @Transactional
    @Override
    public void update(Author author) {
        this.em.merge(author);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.authorRepo.deleteById(id);
    }
}
