package com.dinz.library;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        System.err.println("java-version: " + System.getProperty("java.version"));
        SpringApplication.run(DemoApplication.class, args); 
    }
}
