package com.demo.auth.center.mapper;

import com.demo.auth.center.entity.model.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {

    List<SysPermission> findByUserId(Long userId);

    List<SysPermission> findAll();
}
