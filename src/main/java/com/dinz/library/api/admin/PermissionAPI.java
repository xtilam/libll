package com.dinz.library.api.admin;

import ch.qos.logback.classic.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dinz.library.cache.PermissionCache;
import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.dto.PermissionDTO;
import com.dinz.library.model.Permission;
import com.dinz.library.service.PermissionService;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
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
public class PermissionAPI {

    @Autowired
    PermissionService permissionService;

    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    @Autowired
    PermissionCache permissionCache;

    @PostMapping(value = "/permission")
    public APIResult insertPermission(@RequestBody PermissionDTO perDTO) {
        Permission permission = perDTO.convert();
        boolean isInsertSuccess = false;
        String error = "";

        try {
            isInsertSuccess = permissionCache.addPermission(permission) > 0;
        } catch (javax.persistence.PersistenceException ex) {
            error = APIUtils.getErrorPropertyValue((PropertyValueException) ex.getCause());
        } catch (DataIntegrityViolationException ex) {
            error = APIUtils.getErrorDataIntegrityViolation(ex);
        } catch (Exception e) {
            System.out.println(e);
            error = e.getMessage();
        }

        if (isInsertSuccess) {
            return new APIResult(APIResultMessage.of(APIResultMessage.INSERT_SUCCESS), error);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.INSERT_FAILED), error);
        }
    }

    @GetMapping(value = "/permissions")
    public APIResult getPermissions(PageDTO pageDTO) {
        if (!pageDTO.isUnlimited()) {
            Page<Map<String, Object>> result;
            if (pageDTO.getPage() >= 0) {
                result = permissionService
                        .findWithPagination(PageRequest.of(pageDTO.getPage(), pageDTO.getLimit()));
            } else {
                result = permissionService.findLastPage(pageDTO.getLimit());
            }
            return APIUtils.getAPIResultPage(result);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS), permissionService.findAll());
        }
    }

    @GetMapping(value = "permission")
    public APIResult getPermission(@RequestParam(name = "id") Long id) {
        try {
            return new APIResult(APIResultMessage.SUCCESS, permissionService.findPermissionUpdate(id));
        } catch (Exception e) {
            return new APIResult(APIResultMessage.NOT_FOUND_ITEM, e.toString());
        }
    }

    @DeleteMapping(value = "/permissions")
    public APIResult deletePermission(@RequestBody IdsDTO idsDTO) {
        Long[] ids = idsDTO.getIds();
        if (ids != null) {
            List<Long> deleteFailed = new ArrayList<>();
            List<Long> deleteSuccess = new ArrayList<>();
            for (Long id : ids) {
                if (this.permissionCache.deletePermission(id) > 0) {
                    deleteSuccess.add(id);
                } else {
                    deleteFailed.add(id);
                }
            }
            Map<String, List<Long>> data = new HashMap<>();
            data.put("success", deleteSuccess);
            data.put("fail", deleteFailed);
            return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS), data);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.FAILED), "");
        }
    }

    @PutMapping(value = "/permission")
    public APIResult udpatePermissions(@RequestBody PermissionDTO permissionDTO) {
        Permission convert = permissionDTO.convert();
        boolean isUpdateSuccess = false;
        String error = "";

        try {
            isUpdateSuccess = this.permissionCache.updatePermission(convert) > 0;
        } catch (DataIntegrityViolationException e) {
            error = APIUtils.getErrorDataIntegrityViolation(e);
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                error = APIUtils.getErrorDataIntegrityViolation((ConstraintViolationException) e.getCause());
            } else {
                error = "__" + e.getMessage();
            }

        }

        if (isUpdateSuccess) {
            return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), null);
        } else {
            return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), error);
        }

    }
}
