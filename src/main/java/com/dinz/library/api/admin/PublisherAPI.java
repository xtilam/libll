/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.api.admin;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Publisher;
import com.dinz.library.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DinzeniLL
 */
@RestController
@RequestMapping("/api/admin")
public class PublisherAPI {
    @Autowired
    PublisherService publisherService;
    
    @GetMapping(value = "/publishers")
    public APIResult findCategories(PageDTO dto) {
        if (!dto.isUnlimited()) {
            Page<Publisher> result = null; 
            if(dto.getPage() >= 0){
                result = this.publisherService.getPublishers(PageRequest.of(dto.getPage(), dto.getLimit()));
            }else{
                result = this.publisherService.findLastPage(dto.getLimit());
            }
            return APIUtils.getAPIResultPage(result);
        } else {
            return new APIResult(APIResultMessage.SUCCESS, this.publisherService.getPublishers());
        }
    }
}
