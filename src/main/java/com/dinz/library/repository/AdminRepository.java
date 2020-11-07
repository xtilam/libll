package com.dinz.library.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.dinz.library.model.Admin;
import com.dinz.library.model.GroupAdmin;

@org.springframework.stereotype.Repository
public interface AdminRepository extends Repository<Admin, Long> {

	@Query(value = "select a.id as id, a.fullname as fullname from Admin a")
	public Stream<Object[]> findAllc();

	@Query(value = QueryString.FIND_ALL)
	List<Map<String, Object>> findAllFilter();

	@Query(value = QueryString.FIND_ALL)
	Page<Map<String, Object>> findWithPagination(Pageable pageable);

	@Query(value = QueryString.FIND_BY_ID)
	Map<String, Object> findByIdFilter(@Param("id") Long id);

	@Query(value = QueryString.FIND_BY_ADMIN_CODE)
	Map<String, Object> findByAdminCode(@Param("adminCode") String adminCode);

	@Query(value = QueryString.COUNT_ALL)
	long countFilter();

	@Modifying
	@Query(value = QueryString.UPDATE)
	int update(@Param("id") long id, @Param("email") String email, @Param("fullname") String fullname,
			@Param("gender") int gender, @Param("phone") String phone, @Param("address") String address,
			@Param("dateOfBirth") Date dateOfBirth, @Param("modifiedBy") String modifiedBy,
			@Param("modifiedDate") Date modifiedDate);

	@Query(value = QueryString.NATIVE_GET_PERMISSIONS, nativeQuery = true)
	Set<Long> getAllPermission(@Param("adminCode") String adminCode);

	@Query(value = QueryString.GET_ALL_PERMISSION)
	List<Map<String, Object>> getAllAdminPermission(@Param("adminId") Long id);

	@Query(value = QueryString.GET_ALL_GROUP)
	List<Map<String, Object>> getAllGroup(@Param("adminId") Long id);

	@Modifying
	@Query(value = "delete from PermissionAdmin pa where pa.admin.adminCode in (select a.adminCode from Admin a where a.id=:id)")
	void deleteAllPermissionAdmin(@Param("id") Long adminId);

	@Modifying
	@Query(value = QueryString.DELETE_ALL_GROUP)
	void deleteAllGroupAdmin(@Param("adminCode") String adminCode);

	@Modifying
	@Query(value = "insert into permission_admin(id,admin_code,permission_code) values (?#{#id}, ?#{#adminCode}, ?#{#permissionCode})", nativeQuery = true)
	int insertPermissionAdmin(@Param("id") Long id, @Param("adminCode") String adminCode,
			@Param("permissionCode") String permissionCode);

	@Modifying
	@Query(value = QueryString.NATIVE_INSERT_GROUP_ADMIN, nativeQuery = true)
	int insertGroupAdmin(@Param("gp") GroupAdmin groupAdmin);

	static interface QueryString {

		static String FIND_ALL = "select" //
				+ " a.id as id" //
				+ ",a.adminCode as adminCode" //
				+ ",a.fullname as fullname" //
				+ ",a.identityDocument as identityDocument" //
				+ ",a.email as email" //
				+ ",a.gender as gender" //
				+ ",a.phone as phone" //
				+ ",a.address as address" //
				+ ",a.dateOfBirth as dateOfBirth" //
				+ ",a.createBy as createBy" //
				+ ",a.createDate as createDate" //
				+ ",a.modifiedBy as modifiedBy" //
				+ ",a.modifiedDate as modifiedDate" //
				+ " from Admin a where a.deleteStatus = 0 ";
		static String FIND_BY_ID = FIND_ALL + " and a.id = :id";//
		static String FIND_BY_ADMIN_CODE = FIND_ALL + " and a.adminCode = :adminCode";//
		static String UPDATE = "update Admin a set"//
				+ " a.fullname = :fullname" //
				+ ",a.gender = :gender" //
				+ ",a.phone = :phone" //
				+ ",a.address = :address" //
				+ ",a.dateOfBirth = :dateOfBirth" //
				+ ",a.modifiedBy = :modifiedBy" //
				+ ",a.modifiedDate = :modifiedDate" //
				+ ",a.email = :email" //
				+ " where a.id = :id";
		static String COUNT_ALL = "select count(a.id) from Admin a where a.deleteStatus = 0";
		static String GET_ALL_PERMISSION = "select"//
				+ " p.permissionCode as code"//
				+ ",p.name as name"//
				+ ",p.description as description"//
				+ ",p.id as id"//
				+ ",case when count(a.adminCode) > 0 then true else false end as isAllow" + " from Permission p" //
				+ " left join PermissionAdmin pa on pa.permission.permissionCode = p.permissionCode"//
				+ " left join Admin a on a.id = :adminId and a.deleteStatus = 0 and a.adminCode = pa.admin.adminCode"
				+ " where p.deleteStatus = 0 group by p.id order by isAllow desc";
		static String NATIVE_INSERT_ADMIN_PERMISSION = "";
		static String NATIVE_GET_PERMISSIONS = "select pp.id from permission pp " + "where exists"
				+ "	(select 1 from permission_admin pa where pa.permission_code = pp.permission_code and pa.admin_code = :adminCode)"
				+ "or exists"
				+ "	(select distinct gp.permission_code from group_admin ga inner join group_permission gp on gp.group_code = ga.group_code and ga.admin_code = :adminCode where pp.permission_code = gp.permission_code)";
		static String GET_ALL_GROUP = "select" + " g.groupCode as groupCode"//
				+ ",g.name as name"//
				+ ",g.description as description"//
				+ ",g.createDate as createDate"//
				+ ",g.modifiedDate as modifiedDate"//
				+ ",g.createBy.adminCode as createBy"//
				+ ",g.modifiedBy.adminCode as modifiedBy"//
				+ ",g.id as id"//
				+ ",count(ga.adminCode) > 0 as isAllow"//
				+ " from Group g"//
				+ " left join GroupAdmin ga on ga.group.groupCode = g.groupCode"//
				+ " left join Admin a on a.id = :adminId and a.deleteStatus = 0 and a.adminCode = ga.admin.adminCode"
				+ " where g.deleteStatus = 0"//
				+ " group by g.id " + "order by isAllow desc";
		static String DELETE_ALL_GROUP = "delete GroupAdmin ga where ga.adminCode = :adminCode";
		static String NATIVE_INSERT_GROUP_ADMIN = "insert into group_admin(id, admin_code, group_code) values(?#{#gp.id}, ?#{#gp.adminCode}, ?#{#gp.groupCode})";
	}
}
