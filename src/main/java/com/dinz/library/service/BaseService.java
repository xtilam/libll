package com.dinz.library.service;

import com.dinz.library.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<T> {
    List<T> findAll();

    Page<T> findAll(Pageable page);

    T findById(Long id);

    Page<T> findLastPage(int limit);

    void insert(T entity);

    void update(T entity);

    void delete(Long id);
}
