package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.dinz.library.AdminUserDetails;
import com.dinz.library.common.Utils;
import com.dinz.library.model.Admin;
import com.dinz.library.model.Permission;
import com.dinz.library.repository.PermissionRepository;
import com.dinz.library.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	HttpSession session;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	PermissionRepository permissionRepository;

	public enum QueryString {
		FIND_PERMISSIONS("select "//
				+ " e.permissionCode as code,"//
				+ " e.name as name,"//
				+ " e.description as description,"//
				+ " e.createBy.adminCode as createBy,"//
				+ " e.createDate as createDate,"//
				+ " e.modifiedBy.adminCode as modifiedBy,"//
				+ " e.modifiedDate as createDate,"//
				+ " e.id as id"//
				+ " from Permission e where e.deleteStatus = 0 "),
		FIND_PERMISSION_FOR_UPDATE(FIND_PERMISSIONS.getQuery() + "and e.id = :id"),
		COUNT_PERMISSIONS("select count(e.id) from Permission e"), //
		UPDATE_PERMISSION("update Permission p set"//
				+ " p.name =:name"//
				+ ",p.description =:description"//
				+ ",p.modifiedDate =:modifiedDate"//
				+ ",p.modifiedBy.adminCode =:modifiedBy"//
				+ " where p.id = :id");

		private final String query;

		private QueryString(String query) {
			this.query = query;
		}

		public String getQuery() {
			return query;
		}
	}

	@Transactional
	@Override
	public int insert(Permission permission) {
		Admin admin = ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAdmin();
		Date now = new Date();
		permission.setId(Utils.getRandomId());
		permission.setCreateBy(admin);
		permission.setModifiedBy(admin);
		permission.setCreateDate(now);
		permission.setModifiedDate(now);
		permission.setDeleteStatus(0);
		this.em.persist(permission);
		return 1;
	}

	@Transactional
	@Override
	public int update(Permission permission) {
		Admin admin = ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAdmin();
		return this.em.createQuery(QueryString.UPDATE_PERMISSION.getQuery())//
				.setParameter("name", permission.getName())//
				.setParameter("description", permission.getDescription())//
				.setParameter("modifiedBy", admin.getAdminCode())//
				.setParameter("modifiedDate", new Date())//
				.setParameter("id", permission.getId())//
				.executeUpdate();
	}

	@Transactional
	@Override
	public int delete(Long id) {
		try {
			Map<String, Object> permission = this.findPermissionUpdate(id);
			permission.put("modifiedBy",
					((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
							.getAdmin().getAdminCode());
			return this.permissionRepository.deletePermission(permission);
		} catch (Exception e) {
		}
		return 0;
	}

	private Page<Map<String, Object>> findWithPagination(Pageable pageable, boolean isLastPage) {
		int totalRecords = Integer
				.parseInt(this.em.createQuery(QueryString.COUNT_PERMISSIONS.getQuery()).getSingleResult().toString());
		int maxPage = (totalRecords / pageable.getPageSize()) + (totalRecords % pageable.getPageSize() == 0 ? 0 : 1);

		if (isLastPage && maxPage > 0) {
			pageable = PageRequest.of(maxPage - 1, pageable.getPageSize());
		}

		List<Map<String, Object>> contentOfOutput;
		// Trang có tồn tại
		if (maxPage > pageable.getPageNumber()) {
			TypedQuery<Object[]> tQuery = this.em.createQuery(QueryString.FIND_PERMISSIONS.getQuery(), Object[].class)
					.setMaxResults(pageable.getPageSize())//
					.setFirstResult((int) pageable.getOffset());

			contentOfOutput = tQuery.getResultStream().map(values -> {
				Map<String, Object> item = new HashMap<>();
				item.put("code", values[0]);
				item.put("name", values[1]);
				item.put("description", values[2]);
				item.put("createBy", values[3]);
				item.put("createDate", values[4]);
				item.put("modifiedBy", values[5]);
				item.put("modifiedDate", values[6]);
				item.put("id", values[7]);
				return item;
			}).collect(Collectors.toList());
		} else {
			contentOfOutput = new ArrayList<>();
		}

		return new PageImpl<Map<String, Object>>(contentOfOutput, pageable, totalRecords);
	}

	@Override
	public Map<String, Object> findPermissionUpdate(Long id) {
		Object[] item = this.em.createQuery(QueryString.FIND_PERMISSION_FOR_UPDATE.getQuery(), Object[].class)
				.setParameter("id", id).getSingleResult();
		Map<String, Object> output = new HashMap<>();
		output.put("code", item[0]);
		output.put("name", item[1]);
		output.put("description", item[2]);
		output.put("createBy", item[3]);
		output.put("createDate", item[4]);
		output.put("modifiedBy", item[5]);
		output.put("modifiedDate", item[6]);
		output.put("id", item[7]);
		return output;
	}

	@Override
	public List<Map<String, Object>> findAll() {
		TypedQuery<Object[]> tQuery = this.em.createQuery(QueryString.FIND_PERMISSIONS.getQuery(), Object[].class);

		List<Map<String, Object>> output = tQuery.getResultStream().map(values -> {
			Map<String, Object> item = new HashMap<>();
			item.put("code", values[0]);
			item.put("name", values[1]);
			item.put("description", values[2]);
			item.put("createBy", values[3]);
			item.put("createDate", values[4]);
			item.put("modifiedBy", values[5]);
			item.put("modifiedDate", values[6]);
			item.put("id", values[7]);
			return item;
		}).collect(Collectors.toList());

		return output;
	}

	@Override
	public Page<Map<String, Object>> findWithPagination(Pageable pageable) {
		return this.findWithPagination(pageable, false);
	}

	@Override
	public Page<Map<String, Object>> findLastPage(int limit) {
		return this.findWithPagination(PageRequest.of(0, limit), true);
	}
}