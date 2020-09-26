package com.dinz.library.jwt;

import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dinzenida
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminJWTModel {

    private static String USER_ID_KEY = "adminId";
    private static String TOKEN_ID_KEY = "tokenId";
    private static String USER_CODE_KEY = "adminCode";
    private Long userId;
    private Long tokenId;
    private String userCode; 

    public AdminJWTModel(Claims claims) {
        this.userId = claims.get(USER_ID_KEY, Long.class);
        this.tokenId = claims.get(TOKEN_ID_KEY, Long.class);
        this.userCode = claims.get(USER_CODE_KEY, String.class);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> output = new HashMap<>();
        output.put(USER_ID_KEY, this.userId);
        output.put(TOKEN_ID_KEY, this.tokenId);
        output.put(USER_CODE_KEY, this.userCode);
        return output;
    }
}
