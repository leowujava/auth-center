package com.demo.auth.center.entity.model;

import lombok.Data;

@Data
public class SysPermission extends BaseEntity {

    private Long id;

    /**
     * user:add
     */
    private String permCode;

    private String permName;

    /**
     * /api/user/add
     */
    private String url;

    /**
     * GET POST PUT DELETE
     */
    private String method;

    /**
     * API MENU BUTTON
     */
    private String type;

    private Long parentId;

    private Integer status;
}
