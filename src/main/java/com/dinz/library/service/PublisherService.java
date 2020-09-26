/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.service;

import com.dinz.library.model.Publisher;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author DinzeniLL
 */
public interface PublisherService {

    public List<Publisher> getPublishers();

    public Page<Publisher> getPublishers(Pageable page);
    
    public Page<Publisher> findLastPage(int limit);
}
