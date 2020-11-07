/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.cache;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author DinzeniLL
 */
public interface IAdminLoginSession {

	/**
	 * Cập nhật token nếu hợp lệ
	 * 
	 * @param tokenId    : Mã Id của token
	 * @param req        : Lấy thông tin về lần cuối sử dụng (IP, Browser)
	 * @param adminId    : Id của người dùng quản trị
	 * @param adminCache : Đối tượng lưu trữ phiên đăng nhập
	 * @return
	 */
	boolean updateToken(Long tokenId, HttpServletRequest req, Long adminId, AdminUserLoginCache adminCache);

	/**
	 * Xóa thông tin về các phiên đăng nhập quá lâu không sử dụng
	 * 
	 * @param userId     : Id của người dùng
	 * @param adminCache : Đối tượng lưu trữ phiên đăng nhập
	 */
	void clearCache(Long userId, AdminUserLoginCache adminCache);

	/**
	 * Xóa tất cả các nhóm mà người dùng có, sẽ được cập nhật lại cùng với phương
	 * thức {@link #checkPermission(String, Long)}
	 */
	void clearGroups();

	/**
	 * Xóa tất cả các chức năng mà người dùng có, sẽ được cập nhật lại cùng với
	 * phương thức {@link #checkPermission(String, Long)}
	 */
	void clearPermissions();

	/**
	 * Vô hiệu hóa token
	 * 
	 * @param tokenId : Id của token
	 */
	void removeToken(Long tokenId);

	/**
	 * Trả về token id, để build ra jwt cho người dùng, sử dụng sau khi đã kiểm tra
	 * đăng nhập
	 * 
	 * @param adminLoginInfo : Thông tin về phiên đăng nhập
	 * @return
	 */
	Long addToken(AdminLoginInfo adminLoginInfo);

	/**
	 * Kiểm tra quyền của người dùng
	 * 
	 * @param permission Tên chức năng
	 * @param adminCache Đối tượng lưu trữ thông tin đăng nhập
	 * @return
	 */
	boolean checkPermission(String permission, Long adminId);

}
