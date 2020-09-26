/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.dto;

import java.util.Date;

import com.dinz.library.model.Admin;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author dinzenida
 */
@lombok.Data
public class AdminDTO {

    private Long id;
    private String adminCode;
    private String fullname;
    private String identityDocument;
    private String email;
    private Integer gender;
    private String address;
    private String phone;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String password;   

    public Admin toAdminUser() {
        Admin admin = new Admin();
        admin.setId(this.getId());
        admin.setFullname(this.getFullname());
        admin.setIdentityDocument(this.getIdentityDocument());
        admin.setEmail(this.getEmail());
        admin.setAddress(this.getAddress());
        admin.setPhone(this.getPhone());
        admin.setDateOfBirth(this.getDateOfBirth());
        admin.setAdminCode(this.getAdminCode());
        admin.setGender(this.getGender());
        admin.setPassword(this.getPassword()); 
        return admin;
    }
}
