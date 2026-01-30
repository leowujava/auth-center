package com.demo.auth.center.entity.model;

import lombok.Data;

@Data
public class SysRole extends BaseEntity {

    private Long id;

    /**
     * ADMIN USER
     */
    private String roleCode;

    private String roleName;

    private Integer status;
}
