/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.dto;

import java.util.Set;
import lombok.Data;

/**
 *
 * @author DinzeniLL
 */
@Data
public class AdminGroupsDTO {
    private Long adminId; 
    private Set<Long> groupIds;
}
