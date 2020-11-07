package com.dinz.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.model.Admin;
import java.lang.reflect.Field;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
public class LibApplication {

    private static ApplicationContext context;

    public AdminUserLoginCache getAdminCache() {
        return context.getBean(AdminUserLoginCache.class);
    }

    public static Admin getAdminContext() {
        return ((AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAdmin();
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void main(String[] args) {
        System.err.println("java lending: " + System.getProperty("java.version"));
        context = SpringApplication.run(LibApplication.class, args);
    }
}
