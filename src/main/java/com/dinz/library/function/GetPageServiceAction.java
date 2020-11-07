package com.dinz.library.function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPageServiceAction{
    interface CountAction{
        int countAction();
    }
    interface GetPageAction <T>{
        Page<T> getPage(int page, int limit);
    }
}
