package com.dinz.library;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.model.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	Admin admin;
	AdminUserLoginCache adminCache;
	Long tokenId;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public boolean checkPermission(String permission) {
		return this.adminCache.checkPermission(this.admin.getId(), permission);
	}

	@Override
	public String getUsername() {
		return this.admin.getAdminCode();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return this.admin.getPassword();
	}
}
