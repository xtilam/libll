/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.constants;

/**
 *
 * @author dinzenida
 */
public class SystemConstants {
    //  5 days
    public static final Long MAX_TIME_LIFE_ADMIN_TOKEN = 1000L * 60 * 60 * 24 * 5;
    public static final String ADMIN_JWT_SECRET = "blueGreenIIMaxe";
    public static final Long SLEEP_TIME_CLEAN_CACHE = 1000L * 10;//* 3600 * 12;
    public static final Long MAX_TIME_LIFE_PERMISSIONS_IN_ADMIN_CACHE = 1000L *10; //* 3600 * 12;
    public static final String CROSS_ORIGIN = "http://localhost:3000";
}
