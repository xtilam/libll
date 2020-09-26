package com.dinz.library.dto;

import java.util.Set;
import lombok.Data;

@Data
public class AdminPermissionDTO {
    Long userId;
    Set<Long> permissionIds;
}