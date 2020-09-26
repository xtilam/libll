package com.dinz.library.cache;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Key cho PermissionCache (URL,Method)
 *
 * @author dinzenida
 */
@Data
public class RequestKey {

	private String url;
	private String method;

	public RequestKey(HttpServletRequest req) {
		this.url = req.getRequestURI();
		this.method = req.getMethod();
	}

	public RequestKey(String url, String method) {
		this.url = url;
		this.method = method;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RequestKey) {
			RequestKey requestKey = (RequestKey) o;
			if (this.url.equals(requestKey.getUrl()) && this.method.equals(requestKey.getMethod())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.url.hashCode() ^ this.method.hashCode();
	}
}
