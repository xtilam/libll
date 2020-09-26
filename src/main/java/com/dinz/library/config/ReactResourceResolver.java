/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 *
 * @author dinzenida
 */
public class ReactResourceResolver implements ResourceResolver {

    private static final String REACT_DIR = "/static/";
    private static final String REACT_STATIC_DIR = "static";
    private static final Resource index = new ClassPathResource(REACT_DIR + "index.html");
    private static final Map<String, Boolean> rootStaticFiles = initRootStaticFiles();

    public static Map<String, Boolean> initRootStaticFiles() {
        Map<String, Boolean> rootFiles = new HashMap<>();
        try {
            File[] files = new ClassPathResource(REACT_DIR).getFile().listFiles();
            System.out.println(files.length);
            String indexName = index.getFilename();
            for (File file : files) {
                if(!file.getName().equals(indexName)){
                    rootFiles.put(file.getName(), Boolean.TRUE);
                    System.out.println(file.getName());
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return rootFiles;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {
        return resolve(requestPath, locations);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        Resource resolvedResource = resolve(resourcePath, locations);   
        if (resolvedResource == null) {
            return null;
        }
        try {
            return resolvedResource.getURL().toString();
        } catch (IOException e) {
            return resolvedResource.getFilename();
        }
    }

    private Resource resolve(String requestPath, List<? extends Resource> locations) {
        if (requestPath.startsWith("api")) {
            return null;
        }
        if (Boolean.TRUE.equals(rootStaticFiles.get(requestPath))
                || requestPath.startsWith(REACT_STATIC_DIR)) {
            return new ClassPathResource(REACT_DIR + requestPath);
        } else {
            return index;
        }
    }

}
