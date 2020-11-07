package com.dinz.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dinz.library.model.Author;

@Repository
public interface AuthorRepository extends org.springframework.data.repository.Repository<Author, Long> {
	@Query(value = "select a from Author a where a.deleteStatus = 0 and a.id = :id")
	Author findById(@Param("id") Long id);

	@Query(value = "select a from Author a where a.deleteStatus = 0")
	Page<Author> findAll(Pageable page);

	@Query(value = "select a from Author a where a.deleteStatus = 0")
	List<Author> findAll();

	@Query(value = "select count(a) from Author a where a.deleteStatus = 0")
	Integer countAll();

	@Query(value = "update Author a set a.deleteStatus = 1 where a.id = :id")
    void deleteById(@Param("id") Long id);
}
