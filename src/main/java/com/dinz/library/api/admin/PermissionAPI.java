package com.dinz.library.api.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.dto.PermissionDTO;
import com.dinz.library.model.Permission;
import com.dinz.library.service.PermissionService;

@RestController
@RequestMapping(value = "/api/admin")
public class PermissionAPI {

	@Autowired
	PermissionService permissionService;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	@PostMapping(value = "/permission")
	public APIResult insertPermission(@RequestBody PermissionDTO perDTO) {
		Permission permission = perDTO.convert();
		if (permissionService.insert(permission) > 0) {
			return new APIResult(APIResultMessage.INSERT_FAILED);
		} else {
			return new APIResult(APIResultMessage.INSERT_FAILED);
		}
	}

	@GetMapping(value = "/permissions")
	public APIResult getPermissions(PageDTO pageDTO) {
		if (!pageDTO.isUnlimited()) {
			Page<Map<String, Object>> result;
			if (pageDTO.getPage() >= 0) {
				result = permissionService.findWithPagination(PageRequest.of(pageDTO.getPage(), pageDTO.getLimit()));
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
		return new APIResult(APIResultMessage.SUCCESS, permissionService.findPermissionUpdate(id));
	}

	@DeleteMapping(value = "/permissions")
	public APIResult deletePermission(@RequestBody IdsDTO idsDTO) {
		Long[] ids = idsDTO.getIds();
		if (ids != null) {
			List<Long> deleteFailed = new ArrayList<>();
			List<Long> deleteSuccess = new ArrayList<>();
			for (Long id : ids) {
				if (this.permissionService.delete(id) > 0) {
					deleteSuccess.add(id);
				} else {
					deleteFailed.add(id);
				}
			}
			Map<String, List<Long>> data = new HashMap<>();
			data.put("success", deleteSuccess);
			data.put("fail", deleteFailed);
			return new APIResult(APIResultMessage.SUCCESS, data);
		} else {
			return new APIResult(APIResultMessage.FAILED);
		}
	}

	@PutMapping(value = "/permission")
	public APIResult udpatePermissions(@RequestBody PermissionDTO permissionDTO) {
		Permission convert = permissionDTO.convert();

		if (this.permissionService.update(convert) > 0) {
			return new APIResult(APIResultMessage.UPDATE_SUCCESS);
		} else {
			return new APIResult(APIResultMessage.UPDATE_FAILED);
		}

	}
}
