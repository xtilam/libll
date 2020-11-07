package com.dinz.library.api.admin;

import com.dinz.library.dto.IdsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Category;
import com.dinz.library.repository.CategoryRepository;
import com.dinz.library.service.CategoryService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author DinzeniLL
 */
@RestController
@RequestMapping(value = "/api/admin/book")
public class CategoryAPI {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepo;

    @GetMapping(value = "/categories")
    public APIResult findCategories(PageDTO dto) {
        if (!dto.isUnlimited()) {
            Page<Category> result = null;
            if (dto.getPage() >= 0) {
                result = this.categoryService.findAll(PageRequest.of(dto.getPage(), dto.getLimit()));
            } else {
                result = this.categoryService.findLastPage(dto.getLimit());
            }
            return APIUtils.getAPIResultPage(result);
        } else {
            return new APIResult(APIResultMessage.SUCCESS, categoryService.findAll());
        }
    }

    @GetMapping(value = "/category")
    public APIResult findCategories(@RequestParam("id") Long id) {
        return new APIResult(APIResultMessage.SUCCESS, categoryService.findById(id));
    }

    @PostMapping(value = "/category")
    public APIResult insertCategory(@RequestBody Category category) {
        this.categoryService.update(category);
        return new APIResult(APIResultMessage.INSERT_SUCCESS);
    }

    @PutMapping(value = "/category")
    public APIResult updateCategory(@RequestBody Category category) {
        this.categoryService.update(category);
        return new APIResult(APIResultMessage.UPDATE_SUCCESS);
    }

    @DeleteMapping(value = "/categories")
    public APIResult deleteCategories(@RequestBody IdsDTO idsDTO) {
        return APIUtils.deleteIds((id) -> {
            try {
                this.categoryService.delete(id);
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }, idsDTO.getIds());
    }
}
