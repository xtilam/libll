package com.dinz.library.api.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinz.library.AdminUserDetails;
import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.common.updatemtm.UpdateMTM;
import com.dinz.library.dto.AdminDTO;
import com.dinz.library.dto.AdminGroupsDTO;
import com.dinz.library.dto.AdminLoginDTO;
import com.dinz.library.dto.AdminOtherDTO;
import com.dinz.library.dto.AdminPermissionDTO;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.jwt.AdminJWTProvider;
import com.dinz.library.model.Admin;
import com.dinz.library.service.AdminService;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminAPI {

	@Autowired
	AdminService adminService;

	@Autowired
	AdminUserLoginCache adminCache;

	@Autowired
	AdminJWTProvider tokenProvider;

	@Autowired
	UpdateMTM updateMTM;

	@PersistenceContext
	EntityManager em;

	/**
	 * Người dùng đăng nhập
	 *
	 * @param adminLoginDTO
	 * @return Thông tin người dùng cùng jwt trong APIResult
	 */
	@PostMapping(value = "/login")
	public APIResult adminLogin(@RequestBody AdminLoginDTO adminLoginDTO) {
		Map<String, Object> adminLogin = adminService.checkLogin(adminLoginDTO.toAdmin());
		if (adminLogin != null) {
			Long adminId = (Long) adminLogin.get("id");
			Long tokenId = adminCache.adminUserLogin(adminId);
			if (tokenId != null) {
				String jwt = tokenProvider.generateToken(adminLogin, tokenId);
				Map<String, Object> result = new HashMap<>();
				result.put("data", adminLogin);
				result.put("token", jwt);
				return new APIResult(APIResultMessage.SUCCESS, result);
			}
		}

		return new APIResult(APIResultMessage.FAILED, "");
	}

	/**
	 * @return Danh sách các chức năng hiện của của người dùng hiện tại trong
	 *         APIResult
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/user-permissions")
	public APIResult getAllPermissionOfUser(Authentication authentication) {
		Long userId = ((AdminUserDetails) authentication.getPrincipal()).getAdmin().getId();
		return new APIResult(APIResultMessage.SUCCESS, this.adminCache.getAllPermissions(userId));
	}

	/**
	 * Xóa phiên đăng nhập trong cache
	 *
	 * @return Một lời chào tạm biệt :V trong APIResult
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/logout")
	public APIResult adminLogout(Authentication authentication) {
		AdminUserDetails adminDetail = (AdminUserDetails) authentication.getPrincipal();
		adminCache.removeToken(adminDetail.getAdmin().getId(), adminDetail.getTokenId());
		return new APIResult(APIResultMessage.SUCCESS, "Good bye!");
	}

	/**
	 * @param pageDTO : dto
	 * @return Danh sách tất cả các người dùng quản trị
	 */
	@PreAuthorize("hasPermission('', 'getAdminUser')")
	@GetMapping(value = "/admin-users")
	public APIResult getListAdminUser(PageDTO pageDTO) {
		if (!pageDTO.isUnlimited()) {
			Page<Map<String, Object>> result;
			if (pageDTO.getPage() >= 0)
				result = adminService.findWithPagination(PageRequest.of(pageDTO.getPage(), pageDTO.getLimit()));
			else
				result = adminService.findLastPage(pageDTO.getLimit());
			return APIUtils.getAPIResultPage(result);
		} else
			return new APIResult(APIResultMessage.SUCCESS, adminService.findAll());
	}

	@PreAuthorize("hasPermission('', 'addAdminUser')")
	@PostMapping(value = "/admin-user")
	public APIResult insertUserAdmin(@RequestBody AdminDTO adminDTO) {
		Admin admin = adminDTO.toAdminUser();
		adminService.insert(admin);
		return new APIResult(APIResultMessage.INSERT_SUCCESS, admin.getId());
	}

	@PreAuthorize("hasPermission('', 'updatePermissionAdminUser')")
	@PutMapping(value = "/admin-user/permissions")
	public APIResult updatePermissions(@RequestBody AdminPermissionDTO dto) {
		adminService.updateAdminPermissions(dto.getUserId(), dto.getPermissionIds());
		adminCache.cleanListPermissionOfUser(dto.getUserId());
		return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
	}

	@PreAuthorize("hasPermission('', 'getPermissionAdminUser')")
	@GetMapping(value = "/admin-user/permissions")
	public APIResult getAllPermission(@RequestParam("id") Long adminId) {
		return new APIResult(APIResultMessage.SUCCESS, adminService.getAllAdminPermission(adminId));
	}

	@PreAuthorize("hasPermission('', 'deleteAdminUser')")
	@DeleteMapping(value = "/admin-users")
	public APIResult deleteAdminUsers(@RequestBody IdsDTO idsDTO) {
		Long[] ids = idsDTO.getIds();
		if (ids != null) {
			List<Long> deleteFailed = new ArrayList<>();
			List<Long> deleteSuccess = new ArrayList<>();
			for (Long id : ids)
				if (adminCache.deleteUser(id) > 0)
					deleteSuccess.add(id);
				else
					deleteFailed.add(id);
			Map<String, List<Long>> data = new HashMap<>();
			data.put("success", deleteSuccess);
			data.put("fail", deleteFailed);
			return new APIResult(APIResultMessage.of(APIResultMessage.SUCCESS), data);
		} else
			return new APIResult(APIResultMessage.of(APIResultMessage.FAILED), "");
	}

	@PreAuthorize("hasPermission('', 'updateAdminUser')")
	@PutMapping(value = "/admin-user")
	public APIResult updateAdminUser(@RequestBody AdminDTO adminDTO) {
		Admin admin = adminDTO.toAdminUser();
		adminService.update(admin);
		return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS));
	}

	@PreAuthorize("hasPermission('', 'getAdminUser')")
	@GetMapping(value = "/admin-user")
	public APIResult getAdminUser(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "adminCode", required = false) String adminCode) {
		Map<String, Object> admin = null;

		if (null != id)
			admin = this.adminService.findAdminUser(id);
		else if (null != adminCode)
			admin = this.adminService.findAdminUserByAdminCode(adminCode);
		else
			return new APIResult(APIResultMessage.NOT_FOUND_ITEM, null);

		if (null != admin.get("id"))
			return new APIResult(APIResultMessage.SUCCESS, admin);
		else
			return new APIResult(APIResultMessage.NOT_FOUND_ITEM, null);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/change-password")
	public APIResult changePassword(AdminOtherDTO.ChangePassword changePasword) {
		if (this.adminService.changePassword(changePasword.getOldPassword(), changePasword.getNewPassword()) > 0)
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), "");
		else
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), "");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/upload-avatar")
	public APIResult updateAvatar(@RequestParam("image") MultipartFile image) {
		Admin admin = ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAdmin();
		try {
			admin.uploadImage(image);
			return new APIResult(APIResultMessage.SUCCESS, "");
		} catch (IOException e) {
			return new APIResult(APIResultMessage.FAILED, "Hình ảnh không hợp lệ", null);
		}
	}

	@PreAuthorize("hasPermission('', 'updateAdminUser')")
	@PutMapping(value = "/admin-user/upload-avatar")
	public APIResult updateAvatarByID(@RequestParam Long id, @RequestParam("image") MultipartFile image) {
		if (((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdmin()
				.getId() != id && id > 0)
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
		else
			return new APIResult(APIResultMessage.ACCESS_DENIED, null);
	}

	@PreAuthorize("hasPermission('', 'resetPasswordAdminUser')")
	@PutMapping(value = "/admin-user/reset-password")
	public APIResult resetPassword(@RequestBody AdminOtherDTO.ResetPasword resetPaswordDTO) {
		String error = "";
		if (((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdmin()
				.getId().equals(resetPaswordDTO.getUserId()))
			error = "Vui lòng dùng chức năng tự thay đổi mật khẩu! ";
		else if (this.adminService.resetPassword(resetPaswordDTO.getUserId(), resetPaswordDTO.getPassword()) > 0)
			return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_SUCCESS), "");
		return new APIResult(APIResultMessage.of(APIResultMessage.UPDATE_FAILED), error);
	}

	@PreAuthorize("hasPermission('', 'getPermissionAdminUser')")
	@GetMapping(value = "/admin-user/groups")
	public APIResult getAllGroup(@RequestParam("id") Long id) {
		return new APIResult(APIResultMessage.SUCCESS, this.adminService.getAllGroup(id));
	}

	@PreAuthorize("hasPermission('', 'updatePermissionAdminUser')")
	@PutMapping(value = "/admin-user/groups")
	public APIResult updateAllGroup(@RequestBody AdminGroupsDTO dto) {
		try {
			this.adminService.updateAdminGroups(dto.getAdminId(), dto.getGroupIds());
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
		} catch (Exception e) {
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, e.toString());
		}
	}

	@GetMapping("/test")
	public Object test(@RequestParam(name = "name", required = false) String name) {
		return new Object[] { this.updateMTM.retilationMTMFound,
				this.em.createQuery("select categoryCode from Category where id = ?1 and deleteStatus = 0")
						.setParameter(1, 1L).getSingleResult() };
	}
}
