/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.api.admin;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Publisher;
import com.dinz.library.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author DinzeniLL
 */
@RestController
@RequestMapping("/api/admin/book")
public class PublisherAPI {
    @Autowired
    PublisherService publisherService;

    @GetMapping(value = "/publishers")
    public APIResult findAll(PageDTO dto) {
        if (!dto.isUnlimited()) {
            Page<Publisher> result = null;
            if (dto.getPage() >= 0) {
                result = this.publisherService.findAll(PageRequest.of(dto.getPage(), dto.getLimit()));
            } else {
                result = this.publisherService.findLastPage(dto.getLimit());
            }
            return APIUtils.getAPIResultPage(result);
        } else {
            return new APIResult(APIResultMessage.SUCCESS, this.publisherService.findAll());
        }
    }

    @PostMapping(value = "/publisher")
    public APIResult insertCategory(@RequestBody Publisher publisher) {
        this.publisherService.update(publisher);
        return new APIResult(APIResultMessage.INSERT_SUCCESS);
    }

    @GetMapping(value = "/publisher")
    public APIResult findPublisher(@RequestParam("id") Long id) {
        return new APIResult(APIResultMessage.SUCCESS, publisherService.findById(id));
    }

    @PutMapping(value = "/publisher")
    public APIResult updateCategory(@RequestBody Publisher publisher) {
        this.publisherService.update(publisher);
        return new APIResult(APIResultMessage.UPDATE_SUCCESS);
    }

    @DeleteMapping(value = "/publishers")
    public APIResult deleteCategories(@RequestBody IdsDTO idsDTO) {
        return APIUtils.deleteIds((id) -> {
            try {
                this.publisherService.delete(id);
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }, idsDTO.getIds());
    }
}
