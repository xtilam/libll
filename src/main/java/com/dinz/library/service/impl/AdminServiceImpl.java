package com.dinz.library.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.dinz.library.common.Utils;
import com.dinz.library.model.Admin;
import com.dinz.library.model.GroupAdmin;
import com.dinz.library.model.IRegexPatternCheckData;
import com.dinz.library.repository.AdminRepository;
import com.dinz.library.service.AdminService;
import com.dinz.library.service.GroupService;
import com.dinz.library.service.PermissionService;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PermissionService permissionService;
    
    @Autowired
    GroupService groupService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    HttpSession session;

    @Override
    public Map<String, Object> findAdminUser(Long id) {
        return this.adminRepository.findByIdFilter(id);
    }

    @Transactional
    @Override
    public int resetPassword(Long id, String password) {
        if (id > 0) {
            return this.em.createQuery(QueryString.RESET_PASSWORD).setParameter("id", id)
                    .setParameter("modifiedDate", new Date()).setParameter("newPassword", password)
                    .setParameter("modifiedBy", this.session.getAttribute("adminCode").toString()).executeUpdate();
        }
        return 0;
    }

    @Override
    public Map<String, Object> checkLogin(Admin admin) {
        TypedQuery<Object[]> typedQuery = null;
        String query = AdminRepository.QueryString.FIND_ALL;

        if (null != admin.getEmail() && IRegexPatternCheckData.EMAIL.matcher(admin.getEmail()).find()) {
            query += "and a.email = :email and a.password = : password";
            typedQuery = this.em.createQuery(query, Object[].class);
            typedQuery.setParameter("email", admin.getEmail());
        } else if (null != admin.getAdminCode() && IRegexPatternCheckData.CODE.matcher(admin.getAdminCode()).find()) {
            query += "and a.adminCode = :username and a.password = : password";
            typedQuery = this.em.createQuery(query, Object[].class).setParameter("username", admin.getAdminCode());
        } else {
            return null;
        }

        List<Object[]> resultList = typedQuery.setParameter("password", admin.getPassword()).getResultList();
        if (!resultList.isEmpty()) {
            return getResultQueryItemInFindAll(resultList.get(0));
        } else {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> findAll() {
        return this.adminRepository.findAllFilter();
    }

    @Override
    public Page<Map<String, Object>> findWithPagination(Pageable pageable) {
        return this.adminRepository.findWithPagination(pageable);
    }

    @Override
    public Page<Map<String, Object>> findLastPage(int limit) {
        long totalRecord = this.adminRepository.countFilter();
        int page = (int) (totalRecord / limit + (totalRecord % limit == 0 ? -1 : 0));
        return this.adminRepository.findWithPagination(PageRequest.of(page, limit));
    }

    @Transactional
    @Override
    public int insert(Admin adminUser) {
        Date createDate = new Date();
        Admin admin = new Admin((Long) this.session.getAttribute("adminId"),
                this.session.getAttribute("adminCode").toString());
        adminUser.setId(Utils.getRandomId());
        adminUser.setCreateBy(admin.getAdminCode());
        adminUser.setModifiedBy(admin.getAdminCode());
        adminUser.setDeleteStatus(0);
        adminUser.setCreateDate(createDate);
        adminUser.setModifiedDate(createDate);
        this.em.persist(adminUser);
        return 1;
    }

    @Override
    public int update(Admin adminUser) {
        return this.adminRepository.update(adminUser.getId(), adminUser.getEmail(), adminUser.getFullname(),
                adminUser.getGender(), adminUser.getPhone(), adminUser.getAddress(), adminUser.getDateOfBirth(),
                session.getAttribute("adminCode").toString(), new Date());
    }

    @Transactional
    @Override
    public int changePassword(String oldPassword, String newPassword) {
        return this.em.createQuery(QueryString.CHANGE_PASSWORD)//
                .setParameter("id", this.session.getAttribute("adminId"))//
                .setParameter("newPassword", newPassword)//
                .setParameter("oldPassword", oldPassword)//
                .executeUpdate();
    }

    @Transactional
    @Override
    public int delete(Long id) {
        if (id > 0) {
            Map<String, Object> admin = adminRepository.findByIdFilter(id);
            return this.em.createQuery(QueryString.DELETE)
                    .setParameter("deleteStatus", 2)
                    .setParameter("id", id)
                    .setParameter("modifiedBy", this.session.getAttribute("adminCode"))
                    .setParameter("modifiedDate", new Date())
                    .executeUpdate();

        }
        return 0;
    }

    private Map<String, Object> getResultQueryItemInFindAll(Object[] values) {
        Map<String, Object> output = new HashMap<>();
        output.put("id", values[0]);
        output.put("adminCode", values[1]);
        output.put("fullname", values[2]);
        output.put("identityDocument", values[3]);
        output.put("email", values[4]);
        output.put("gender", values[5]);
        output.put("phone", values[6]);
        output.put("address", values[7]);
        output.put("dateOfBirth", values[8]);
        output.put("createBy", values[9]);
        output.put("createDate", values[10]);
        output.put("modifiedBy", values[11]);
        output.put("modifiedDate", values[12]);
        return output;
    }

    @Transactional
    @Override
    public void updateAdminPermissions(Long adminId, Set<Long> permissionCodes) {
        String adminCode = this.adminRepository.findByIdFilter(adminId).get("adminCode").toString();
        int size = permissionCodes.size();
        String[] permissions = new String[size];
        int count = 0;
        for(Iterator<Long> iterator = permissionCodes.iterator(); iterator.hasNext();){
            permissions[count++] = this.permissionService.findPermissionUpdate(iterator.next()).get("code").toString();
        }
        this.adminRepository.deleteAllPermissionAdmin(adminId);
        for (String pCode : permissions) {
            this.adminRepository.insertPermissionAdmin(Utils.getRandomId(), adminCode, pCode);
        }
        this.em.createQuery("update Admin a set a.modifiedBy = :modifiedBy, a.modifiedDate = :modifiedDate")//
                .setParameter("modifiedBy", this.session.getAttribute("adminCode"))
                .setParameter("modifiedDate", new Date())
                .executeUpdate();
    }

    @Override
    public Set<Long> getAllPermission(String adminCode) {
        return adminRepository.getAllPermission(adminCode);
    }

    @Override
    public Map<String, Object> findAdminUserByAdminCode(String adminCode) {
        return adminRepository.findByAdminCode(adminCode);
    }

    @Override
    public List<Map<String, Object>> getAllAdminPermission(Long adminId) {
        return this.adminRepository.getAllAdminPermission(adminId); 
    }

    @Override
    public List<Map<String, Object>> getAllGroup(String adminCode) {
//        this.em.createQuery("select A.id as id, A.address as ads from Admin A JOIN FETCH A.groupAdmin ga where ga.");
        return this.adminRepository.getAllGroup(adminCode);
    }

    @Override
    public void updateAdminGroups(Long adminId, Set<Long> groupIds) {
        String adminCode = this.adminRepository.findByIdFilter(adminId).get("adminCode").toString();
        int size = groupIds.size();
        String[] groups = new String[size];
        int count = 0;
        for(Iterator<Long> iterator = groupIds.iterator(); iterator.hasNext();){
            groups[count++] = this.groupService.findGroupUseForUpdate(iterator.next()).get("groupCode").toString();
        }
        this.adminRepository.deleteAllGroupAdmin(adminCode);
        for (String gCode : groups) {
            this.adminRepository.insertGroupAdmin(GroupAdmin.createNew(gCode, adminCode));
        }
        this.em.createQuery("update Admin a set a.modifiedBy = :modifiedBy, a.modifiedDate = :modifiedDate")//
                .setParameter("modifiedBy", this.session.getAttribute("adminCode"))
                .setParameter("modifiedDate", new Date()) 
                .executeUpdate(); 
    }

    static class QueryString {

        static String CHANGE_PASSWORD = "update Admin a set"//
                + " a.password = :newPassword"//
                + " where a.id = :id and a.password = :oldPassword";
        static String RESET_PASSWORD = "update Admin a set"//
                + " a.password = :newPassword"//
                + ",a.modifiedDate = :modifiedDate"//
                + ",a.modifiedBy = :modifiedBy"//
                + " where a.id = :id";//
        static String DELETE = "update Admin a set"//
                + " a.deleteStatus = :deleteStatus" //
                + ",a.modifiedBy = :modifiedBy"
                + ",a.modifiedDate = :modifiedDate"
                + " where a.id = :id";
    }

}
