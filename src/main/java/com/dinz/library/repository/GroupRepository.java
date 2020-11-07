package com.dinz.library.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.dinz.library.model.Group;

@org.springframework.stereotype.Repository
public interface GroupRepository extends Repository<Group, Long> {

	@Modifying
	@Query(value = QueryString.DELETE_ALL_GROUP_PERMISSION)
	public void deleteAllGroupPermission(@Param("groupCode") String groupCode);

	@Modifying
	@Query(value = QueryString.NATIVE_INSERT_GROUP_PERMISSION, nativeQuery = true)
	public void insertGroupPermisison(@Param("id") Long id, @Param("groupCode") String groupCode,
			@Param("permissionCode") String permissionCode);

	@Query(value = QueryString.GET_ALL_PERMISSION_IN_GROUP)
	public List<Map<String, Object>> getAllPermissionInGroup(@Param("groupId") Long groupId);

	static class QueryString {
		static final String DELETE_ALL_GROUP_PERMISSION = "delete GroupPermission gp where gp.group.groupCode = :groupCode";
		static final String NATIVE_INSERT_GROUP_PERMISSION = "insert into group_permission(id, group_code, permission_code) values(:id, :groupCode, :permissionCode)";
		static final String GET_ALL_PERMISSION_IN_GROUP = "select "//
				+ " p.permissionCode as code"//
				+ ",p.name as name"//
				+ ",p.description as description"//
				+ ",p.createBy.adminCode as createBy"//
				+ ",p.createDate as createDate"//
				+ ",p.modifiedBy.adminCode as modifiedBy"//
				+ ",p.modifiedDate as createDate"//
				+ ",p.id as id"//
				+ ",count(g.id) > 0 as isAllow" + " from Permission p"
				+ " left join GroupPermission gp on gp.permission.permissionCode = p.permissionCode"
				+ " left join Group g on g.groupCode = gp.group.groupCode and g.id = :groupId"
				+ " where p.deleteStatus = 0 group by p.id";
	}
}
