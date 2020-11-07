package com.dinz.library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.dinz.library.AdminUserDetails;
import com.dinz.library.common.Utils;
import com.dinz.library.model.Admin;
import com.dinz.library.model.Group;
import com.dinz.library.repository.GroupRepository;
import com.dinz.library.service.GroupService;
import com.dinz.library.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	HttpSession session;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	GroupRepository groupRepostitory;

	@Autowired
	PermissionService permissionService;

	@Transactional
	@Override
	public void updateGroupPermission(Long id, Set<Long> permissionIds) {
		String groupCode = this.findById(id).get("groupCode").toString();
		String[] permissionCodes = new String[permissionIds.size()];
		int count = 0;
		this.groupRepostitory.deleteAllGroupPermission(groupCode);
		for (Iterator<Long> iterator = permissionIds.iterator(); iterator.hasNext();) {
			permissionCodes[count++] = permissionService.findPermissionUpdate(iterator.next()).get("code").toString();
		}
		for (String pCode : permissionCodes) {
			groupRepostitory.insertGroupPermisison(Utils.getRandomId(), groupCode, pCode);
		}
	}

	private Page<Map<String, Object>> findWithPagination(Pageable pageable, boolean isLastPage) {
		int totalRecords = Integer
				.parseInt(this.em.createQuery(QueryString.COUNT_ALL_GROUP.getQuery()).getSingleResult().toString());
		int maxPage = (totalRecords / pageable.getPageSize()) + (totalRecords % pageable.getPageSize() == 0 ? 0 : 1);
		if (isLastPage && maxPage > 0) {
			pageable = PageRequest.of(maxPage - 1, pageable.getPageSize());
		}
		List<Map<String, Object>> contentOutput = new ArrayList<>();
		if (maxPage > pageable.getPageNumber()) {
			contentOutput = this.em.createQuery(QueryString.FIND_ALL.getQuery(), Object[].class)
					.setFirstResult((int) pageable.getOffset())//
					.setMaxResults(pageable.getPageSize())//
					.getResultStream()//
					.map(value -> {
						Map<String, Object> item = new HashMap<>();
						item.put("id", value[7]);
						item.put("groupCode", value[0]);
						item.put("name", value[1]);
						item.put("description", value[2]);
						item.put("createDate", value[3]);
						item.put("modifiedDate", value[4]);
						item.put("createBy", value[5]);
						item.put("modifiedBy", value[6]);
						return item;
					}).collect(Collectors.toList());

		}
		return new PageImpl<>(contentOutput, pageable, totalRecords);
	}

	@Transactional
	@Override
	public int insert(Group group) {
		Admin admin = ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAdmin();
		Date insertDate = new Date();
		group.setCreateBy(admin);
		group.setModifiedBy(admin);
		group.setCreateDate(insertDate);
		group.setDeleteStatus(0);
		group.setModifiedDate(insertDate);
		this.em.persist(group);
		return 1;
	}

	@Transactional
	@Override
	public int update(Group group) {
		Admin admin = ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAdmin();
		return this.em.createQuery(QueryString.UPDATE_GROUP.getQuery())//
				.setParameter("id", group.getId())//
				.setParameter("name", group.getName())//
				.setParameter("description", group.getDescription())//
				.setParameter("modifiedBy", admin.getAdminCode())//
				.setParameter("modifiedDate", new Date()).executeUpdate();
	}

	@Transactional
	@Override
	public int delete(Long id) {
		return this.em.createQuery(QueryString.DELETE_GROUP.getQuery())//
				.setParameter("id", id)//
				.setParameter("modifiedBy",
						((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
								.getAdmin().getAdminCode())//
				.setParameter("modifiedDate", new Date()).executeUpdate();
	}

	@Override
	public Page<Map<String, Object>> findWithPagination(Pageable pageable) {
		return this.findWithPagination(pageable, false);
	}

	@Override
	public Page<Map<String, Object>> findLastPage(int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		return this.findWithPagination(pageable, true);
	}

	@Override
	public List<Map<String, Object>> findAll() {
		List<Map<String, Object>> output = this.em.createQuery(QueryString.FIND_ALL.getQuery(), Object[].class)//
				.getResultStream()//
				.map(value -> {
					Map<String, Object> item = new HashMap<>();
					item.put("id", value[7]);
					item.put("groupCode", value[0]);
					item.put("name", value[1]);
					item.put("description", value[2]);
					item.put("createDate", value[3]);
					item.put("modifiedDate", value[4]);
					item.put("createBy", value[5]);
					item.put("modifiedBy", value[6]);
					return item;
				}).collect(Collectors.toList());
		return output;
	}

	@Override
	public List<Map<String, Object>> getAllGroupPermissions(Long groupId) {
		return groupRepostitory.getAllPermissionInGroup(groupId);
	}

	private enum QueryString {
		FIND_ALL("select " //
				+ " g.groupCode as groupCode"//
				+ ",g.name as name"//
				+ ",g.description as description"//
				+ ",g.createDate as createDate"//
				+ ",g.modifiedDate as modifiedDate"//
				+ ",g.createBy.adminCode as createBy"//
				+ ",g.modifiedBy.adminCode as modifiedBy"//
				+ ",g.id as id"//
				+ " from Group g where g.deleteStatus=0 "), //
		DELETE_GROUP(
				"update Group g set g.deleteStatus = 1, g.modifiedBy.adminCode = :modifiedBy, modifiedDate =: modifiedDate where g.id=:id"), //
		FIND_GROUP_FOR_UPDATE(QueryString.FIND_ALL.query + "and g.id = :id"), UPDATE_GROUP("update Group g set "//
				+ " g.name = :name" //
				+ ",g.description = :description"//
				+ ",g.modifiedDate = :modifiedDate"//
				+ ",g.modifiedBy.adminCode = :modifiedBy"//
				+ " where g.id = :id"),
		COUNT_ALL_GROUP("select count(g.id) from Group g");

		private final String query;

		QueryString(String query) {
			this.query = query;
		}

		public String getQuery() {
			return query;
		}

	}

	@Override
	public Map<String, Object> findById(Long id) {
		Map<String, Object> output = new HashMap<>();
		Object[] value = this.em.createQuery(QueryString.FIND_GROUP_FOR_UPDATE.getQuery(), Object[].class)
				.setParameter("id", id).getSingleResult();
		output.put("id", value[7]);
		output.put("groupCode", value[0]);
		output.put("name", value[1]);
		output.put("description", value[2]);
		output.put("createDate", value[3]);
		output.put("modifiedDate", value[4]);
		output.put("createBy", value[5]);
		output.put("modifiedBy", value[6]);
		return output;
	}
}
