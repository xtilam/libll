/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.jwt;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.dinz.library.constants.SystemConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author dinzenida
 */
@Component
public class AdminJWTProvider {

    private static Long MAX_TIME = 1000L * 3600 * 24 * 365 * 10;

    @Autowired
    HttpSession session;

    public String generateToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + MAX_TIME);
        AdminJWTModel adminJWTModel = new AdminJWTModel(
                (Long) session.getAttribute("adminId"),
                (Long) session.getAttribute("tokenId"),
                (String) session.getAttribute("adminCode")
        );
        String jwt = Jwts.builder()//
                .setClaims(adminJWTModel.toMap())
                .setIssuedAt(now)//
                .setExpiration(expiryDate)//
                .signWith(SignatureAlgorithm.HS512, SystemConstants.ADMIN_JWT_SECRET)// 
                .compact();
        return jwt;
    }

    public AdminJWTModel decodeToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SystemConstants.ADMIN_JWT_SECRET).parseClaimsJws(token).getBody();
            return new AdminJWTModel(claims);
        } catch (Exception ex) {
        }
        return null;
    }
}
