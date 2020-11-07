package com.dinz.library;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.jwt.AdminJWTProvider;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private AdminJWTProvider jwtProvider;

	@Autowired
	private AdminUserLoginCache adminCache;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse resp, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = request.getHeader("Authorization");
			if (jwt != null) {
				AdminUserDetails adminUserDetails = (AdminUserDetails) jwtProvider.decodeToken(jwt);
				if (adminUserDetails != null //
						&& adminCache.checkTokenValidAndUpdateIt(//
								adminUserDetails.getAdmin().getId(), //
								adminUserDetails.getTokenId())) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							adminUserDetails, null, adminUserDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
		}
		filterChain.doFilter(request, resp);
	}

}
