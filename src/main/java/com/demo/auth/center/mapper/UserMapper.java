package com.demo.auth.center.mapper;

import com.demo.auth.center.entity.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    SysUser findByUsername(String username);
}
