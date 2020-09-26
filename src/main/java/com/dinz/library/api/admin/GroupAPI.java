package com.dinz.library.api.admin;

import com.dinz.library.cache.AdminUserLoginCache;
import java.util.HashMap;
import java.util.Map;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.GroupDTO;
import com.dinz.library.dto.GroupPermissionDTO;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Group;
import com.dinz.library.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin")
public class GroupAPI {

    @Autowired
    GroupService groupService;
    
    @Autowired
    AdminUserLoginCache adminLogin;

    @PostMapping(value = "/group")
    public APIResult insertGroup(@RequestBody GroupDTO groupDTO) {
        Group group = groupDTO.toGroup();

        boolean isInsertSuccess = false;
        String error = "";

        try {
            isInsertSuccess = groupService.insert(group) > 0;
        } catch (Exception e) {
            error = e.getMessage();
        }

        if (isInsertSuccess) {
            return new APIResult(APIResultMessage.of(APIResultMessage.INSERT_SUCCESS), null);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.INSERT_FAILED), error);
        }
    }

    @PutMapping(value = "/group/permissions")
    public APIResult updateGroupPermissions(@RequestBody GroupPermissionDTO dto) {
        try {
            this.groupService.updateGroupPermission(dto.getGroupId(), dto.getPermissionIds());
            this.adminLogin.clearAllPermission();
            return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
        } catch (Exception e) {
            return new APIResult(APIResultMessage.FAILED, e.toString());
        }
    }

    @PutMapping(value = "/group")
    public APIResult updateGroup(@RequestBody GroupDTO groupDTO) {
        Group group = groupDTO.toGroup();

        boolean isUpdateSuccess = false;
        String error = "";

        try {
            isUpdateSuccess = groupService.update(group) > 0;
        } catch (DataIntegrityViolationException e) {
            error = APIUtils.getErrorDataIntegrityViolation(e);
        } catch (Exception e) {
            error = e.getMessage();
        }

        if (isUpdateSuccess) {
            return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), null);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), error);
        }
    }

    @DeleteMapping(value = "/groups")
    public APIResult deleteGroupsByGroupCode(@RequestBody IdsDTO idsDTO) {
        Map<Long, Boolean> rs = new HashMap<>();
        for (Long id : idsDTO.getIds()) {
            rs.put(id, groupService.delete(id) > 0);
        }
        adminLogin.clearAllPermission();
        return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS), rs);
    }

    @GetMapping(value = "/groups")
    public APIResult findGroups(PageDTO pageDTO) {
        if (!pageDTO.isUnlimited()) {
            Page<Map<String, Object>> rs;
            if (pageDTO.getPage() < 0) {
                rs = groupService.findLastPage(pageDTO.getLimit());
            } else {
                rs = groupService.findWithPagination(PageRequest.of(pageDTO.getPage(), pageDTO.getLimit()));
            }
            return APIUtils.getAPIResultPage(rs);

        } else {
            return new APIResult(APIResultMessage.SUCCESS, groupService.findAll());
        }

    }

    @GetMapping(value = "/group/permissions")
    public APIResult getAllPermissionInGroup(@RequestParam("id") Long id){
        return new APIResult(APIResultMessage.SUCCESS, groupService.getAllGroupPermissions(id));
    }
    
    @GetMapping(value = "/group")
    public APIResult findGroupUseForUpdate(@RequestParam(name = "id") Long id) {
        Map<String, Object> result = groupService.findGroupUseForUpdate(id);
        if (null != result) {
            return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS), result);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.NOT_FOUND_ITEM), null);
        }
    }
}
