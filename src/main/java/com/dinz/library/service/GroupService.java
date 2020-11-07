package com.dinz.library.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dinz.library.model.Group;

public interface GroupService {
	public Page<Map<String, Object>> findWithPagination(Pageable pageable);

	public Page<Map<String, Object>> findLastPage(int limit);

	public List<Map<String, Object>> findAll();

	public int insert(Group group);

	public int update(Group group);

	public int delete(Long id);

	public void updateGroupPermission(Long id, Set<Long> permissionIds);

	public Map<String, Object> findById(Long id);

	public List<Map<String, Object>> getAllGroupPermissions(Long groupId);

}