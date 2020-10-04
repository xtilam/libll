package com.dinz.library.api.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.cache.PermissionCache;
import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.AdminDTO;
import com.dinz.library.dto.AdminGroupsDTO;
import com.dinz.library.dto.AdminLoginDTO;
import com.dinz.library.dto.AdminOtherDTO;
import com.dinz.library.dto.AdminPermissionDTO;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.jwt.AdminJWTProvider;
import com.dinz.library.model.Admin;
import com.dinz.library.repository.AdminRepository;
import com.dinz.library.service.AdminService;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminAPI {

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	@Autowired
	AdminService adminService;

	@PersistenceContext
	EntityManager em;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	PermissionCache permissionCache;

	@Autowired
	AdminUserLoginCache adminCache;

	@Autowired
	AdminJWTProvider tokenProvider;

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(value = "/login")
	public APIResult adminLogin(@RequestBody AdminLoginDTO adminLoginDTO) {
		Map<String, Object> adminLogin = adminService.checkLogin(adminLoginDTO.toAdmin());
		if (adminLogin != null) {
			session.setAttribute("adminId", adminLogin.get("id"));
			session.setAttribute("adminCode", adminLogin.get("adminCode"));
			if (adminCache.adminUserLogin()) {
				String token = tokenProvider.generateToken();
				Map<String, Object> result = new HashMap<>();

				result.put("token", token);
				result.put("user", adminLogin);
				return new APIResult(APIResultMessage.SUCCESS, result);
			}

		}
		return new APIResult(APIResultMessage.FAILED, "");
	}

	@GetMapping(value = "/admin-cache")
	public APIResult getAllCache() {
		return new APIResult(APIResultMessage.SUCCESS,
				new Object[] { adminCache.getCacheMap(), permissionCache.permissionCache });
	}

	@GetMapping(value = "/user-permission")
	public APIResult getPermission() {
		return new APIResult(APIResultMessage.SUCCESS, adminCache.getPermissions());
	}

	@GetMapping(value = "/logout")
	public APIResult adminLogout(HttpSession session) {
		adminCache.removeToken();
		return new APIResult(APIResultMessage.SUCCESS, "Good bye!");
	}

	@GetMapping(value = "/admin-users")
	public APIResult getListAdminUser(PageDTO pageDTO) {
		if (!pageDTO.isUnlimited()) {
			Page<Map<String, Object>> result;
			if (pageDTO.getPage() >= 0) {
				result = adminService.findWithPagination(PageRequest.of(pageDTO.getPage(), pageDTO.getLimit()));
			} else {
				result = adminService.findLastPage(pageDTO.getLimit());
			}
			return APIUtils.getAPIResultPage(result);
		} else {
			return new APIResult(APIResultMessage.SUCCESS, adminService.findAll());
		}
	}

	@PostMapping(value = "/admin-user")
	public APIResult insertUserAdmin(@RequestBody AdminDTO adminDTO) {
		Admin admin = adminDTO.toAdminUser();
		boolean isInsertSuccess = false;
		String error = "";

		try {
			isInsertSuccess = adminService.insert(admin) > 0;
		} catch (DataIntegrityViolationException e) {
			error = APIUtils.getErrorDataIntegrityViolation(e);
		}

		if (isInsertSuccess) {
			return new APIResult(APIResultMessage.INSERT_SUCCESS, admin.getId());
		} else {
			return new APIResult(APIResultMessage.INSERT_FAILED, error);
		}
	}

	@PutMapping(value = "/admin-user/permissions")
	public APIResult updatePermissions(@RequestBody AdminPermissionDTO dto) {
		try {
			adminCache.updateAdminPermissions(dto.getUserId(), dto.getPermissionIds());
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
		} catch (Exception e) {
			return new APIResult(APIResultMessage.UPDATE_FAILED, e.getMessage());
		}

	}

	@GetMapping(value = "/admin-user/permissions")
	public APIResult getAllPermission(@RequestParam("id") Long adminId) {
		return new APIResult(APIResultMessage.SUCCESS, adminService.getAllAdminPermission(adminId));
	}

	@DeleteMapping(value = "/admin-users")
	public APIResult deleteAdminUsers(@RequestBody IdsDTO idsDTO) {
		Long[] ids = idsDTO.getIds();
		if (ids != null) {
			List<Long> deleteFailed = new ArrayList<>();
			List<Long> deleteSuccess = new ArrayList<>();
			for (Long id : ids) {
				if (adminCache.deleteUser(id) > 0) {
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

	@PutMapping(value = "/admin-user")
	public APIResult updateAdminUser(@RequestBody AdminDTO adminDTO) {
		Admin admin = adminDTO.toAdminUser();
		boolean isUpdateSuccess = false;
		String error = "";

		try {
			isUpdateSuccess = adminService.update(admin) > 0;
		} catch (DataIntegrityViolationException e) {
			error = APIUtils.getErrorDataIntegrityViolation(e);
		}

		if (isUpdateSuccess) {
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), error);
		} else {
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), error);
		}
	}

	@GetMapping(value = "/admin-user")
	public APIResult getAdminUser(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "adminCode", required = false) String adminCode) {
		Map<String, Object> admin = null;

		if (null != id) {
			admin = this.adminService.findAdminUser(id);
		} else if (null != adminCode) {
			admin = this.adminService.findAdminUserByAdminCode(adminCode);
		} else {
			return new APIResult(APIResultMessage.NOT_FOUND_ITEM, null);
		}

		if (null != admin.get("id")) {
			return new APIResult(APIResultMessage.SUCCESS, admin);
		} else {
			return new APIResult(APIResultMessage.NOT_FOUND_ITEM, null);
		}
	}

	@PutMapping(value = "/change-password")
	public APIResult changePassword(AdminOtherDTO.ChangePassword changePasword) {
		if (this.adminService.changePassword(changePasword.getOldPassword(), changePasword.getNewPassword()) > 0) {
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), "");
		} else {
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), "");
		}
	}

	@PutMapping(value = "/upload-avatar")
	public APIResult updateAvatar(@RequestParam("image") MultipartFile image) {
		Admin admin = new Admin();
		admin.setId(Long.parseLong(session.getAttribute("adminId").toString()));
		try {
			admin.uploadImage(image);
			return new APIResult(APIResultMessage.SUCCESS, "");
		} catch (IOException e) {
			return new APIResult(APIResultMessage.FAILED, "Hình ảnh không hợp lệ", null);
		}
	}

	@PutMapping(value = "/admin-user/upload-avatar")
	public APIResult updateAvatarByID(@RequestParam Long id, @RequestParam("image") MultipartFile image) {
		if (!(id < 0 && (Long) session.getAttribute("adminId") > 0)) {
			try {
				adminService.findAdminUser(id);
				try {
					Admin admin = new Admin();
					admin.setId(id);
					admin.uploadImage(image);
					return new APIResult(APIResultMessage.SUCCESS, "");
				} catch (IOException e) {
					return new APIResult(APIResultMessage.FAILED, "Hình ảnh không hợp lệ", null);
				}
			} catch (NoResultException e) {
				return new APIResult(APIResultMessage.FAILED, "Không tìm thấy người dùng", null);
			}
		} else {
			return new APIResult(APIResultMessage.ACCESS_DENIED, null);
		}
	}

	@PutMapping(value = "/admin-user/reset-password")
	public APIResult resetPassword(@RequestBody AdminOtherDTO.ResetPasword resetPaswordDTO) {
		String error = "";
		if (session.getAttribute("adminId").equals(resetPaswordDTO.getUserId())) {
			error = "Vui lòng dùng chức năng tự thay đổi mật khẩu!";
		} else if (this.adminService.resetPassword(resetPaswordDTO.getUserId(), resetPaswordDTO.getPassword()) > 0) {
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), "");
		}
		return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), error);
	}

	@GetMapping(value = "/admin-user/groups")
	public APIResult getAllGroup(@RequestParam("adminCode") String adminCode) {
		return new APIResult(APIResultMessage.SUCCESS, this.adminService.getAllGroup(adminCode));
	}

	@PutMapping(value = "/admin-user/groups")
	public APIResult updateAllGroup(@RequestBody AdminGroupsDTO dto) {
		try {
			this.adminService.updateAdminGroups(dto.getAdminId(), dto.getGroupIds());
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
		} catch (Exception e) {
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, e.toString());
		}
	}

	@GetMapping(value = "/test")
	public APIResult test() {
		return new APIResult(APIResultMessage.FAILED, this.adminCache.getCacheMap());
	}
}
