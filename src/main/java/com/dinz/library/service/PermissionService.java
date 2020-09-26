package com.dinz.library.service;

import java.util.List;
import java.util.Map;

import com.dinz.library.model.Permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    public int insert(Permission permission);

    public int update(Permission permission);

    public int delete(Long id);

    public Page<Map<String, Object>> findWithPagination(Pageable pageable);

    public Page<Map<String, Object>> findLastPage(int limit);
    
    public List<Map<String, Object>> findAll();
    
    public Map<String, Object> findPermissionUpdate(Long id);


}