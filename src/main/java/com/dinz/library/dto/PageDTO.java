package com.dinz.library.dto;

import lombok.Data;

@Data
public class PageDTO {

    private Integer page;
    private Integer limit;
    private boolean unlimited;

    private void setPage(int page) {
        this.page = page - 1;
    }

    private void setLimit(int limit) {
        if (limit <= 0) {
            limit = 1;
        }
        this.limit = limit;
    }

    public PageDTO(Integer page, Integer limit, Boolean unlimited) {
        if (page != null) {
            this.setPage(page);
        }
        if (limit != null) {
            this.setLimit(limit);
        }
        this.unlimited = Boolean.TRUE.equals(unlimited);
    }
}
