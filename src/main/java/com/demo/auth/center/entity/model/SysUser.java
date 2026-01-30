package com.demo.auth.center.entity.model;

import lombok.Data;

@Data
public class SysUser extends BaseEntity {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    /**
     * 1正常 0禁用
     */
    private Boolean status;
}
