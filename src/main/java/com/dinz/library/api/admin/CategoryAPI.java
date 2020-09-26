package com.dinz.library.api.admin;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Category;
import com.dinz.library.repository.CategoryRepository;
import com.dinz.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DinzeniLL
 */
@RestController
@RequestMapping(value = "/api/admin")
public class CategoryAPI {

    @Autowired
    CategoryService categoryService;
    
    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping(value = "/categories")
    public APIResult findCategories(PageDTO dto) {
        if (!dto.isUnlimited()) {
            Page<Category> result = null; 
            if(dto.getPage() >= 0){
                result = this.categoryService.findCategories(PageRequest.of(dto.getPage(), dto.getLimit()));
            }else{
                result = this.categoryService.findLastPage(dto.getLimit());
            }
            return APIUtils.getAPIResultPage(result);
        } else {
            return new APIResult(APIResultMessage.SUCCESS, categoryService.findCategories());
        }
    }
}
