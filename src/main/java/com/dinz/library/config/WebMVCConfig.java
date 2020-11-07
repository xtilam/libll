package com.dinz.library.config;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;

import com.dinz.library.ReactResourceResolver;
import com.dinz.library.model.Admin;
import com.dinz.library.model.Book;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse resp, Object handler)
					throws Exception {
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.setHeader("Access-Control-Allow-Headers", "*");
				resp.setHeader("Access-Control-Allow-Methods", "*");
				return true;
			}
		});
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		final ResourceResolver resolver = new ReactResourceResolver();
		registry.addResourceHandler("/**").addResourceLocations("").resourceChain(true).addResolver(resolver);
		registry.addResourceHandler("/admin/images/**")//
				.addResourceLocations("file:" + Admin.PATH_AVARTAR_ADMIN_USER.toFile().getPath() + File.separator)//
				.setCachePeriod(0);
		registry.addResourceHandler("/book/images/**")//
				.addResourceLocations("file:" + Book.PATH_BOOK_COVER.toFile().getPath() + File.separator)//
				.setCachePeriod(0);
	}

}
