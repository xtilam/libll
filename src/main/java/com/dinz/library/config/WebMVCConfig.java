package com.dinz.library.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;

import com.dinz.library.inteceptor.AdminLoginInterceptor;
import com.dinz.library.model.Admin;
import com.dinz.library.model.Book;

@Configuration
//@EnableWebMvc
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/login", "/api/admin/test");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")//
//                .addResourceLocations("file:src/main/resources/static/")//
//                .setCachePeriod(0);
//        registry.addResourceHandler("/ts/**")//
//                .addResourceLocations("file:src/main/resources/ts/")//
//                .setCachePeriod(0);
        final ResourceResolver resolver = new ReactResourceResolver();
        registry.addResourceHandler("/**").addResourceLocations("")
                .resourceChain(true)
                .addResolver(resolver);
        registry.addResourceHandler("/admin/images/**")//
                .addResourceLocations("file:" + Admin.PATH_AVARTAR_ADMIN_USER.toFile().getPath() + File.separator)//
                .setCachePeriod(0);
        registry.addResourceHandler("/book/images/**")//
                .addResourceLocations("file:" + Book.PATH_BOOK_COVER.toFile().getPath() + File.separator)//
                .setCachePeriod(0);
    }

}
