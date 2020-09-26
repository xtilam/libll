/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.repository;

import com.dinz.library.model.Publisher;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DinzeniLL
 */
@Repository
public interface PublisherRepository extends org.springframework.data.repository.Repository<Publisher, Long>{
    @Query(value = "select p from Publisher p where p.deleteStatus = 0")
    public List<Publisher> getPublishers();
    
    @Query(value = "select p from Publisher p where p.deleteStatus = 0")
    public Page<Publisher> getPublishers(Pageable page);
    
    @Query(value = "select count(p.id) from Publisher p where p.deleteStatus = 0")
    public Integer countAll(); 
}
