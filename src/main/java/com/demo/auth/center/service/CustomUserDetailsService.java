package com.demo.auth.center.service;

import com.demo.auth.center.entity.model.SysPermission;
import com.demo.auth.center.entity.model.SysUser;
import com.demo.auth.center.mapper.PermissionMapper;
import com.demo.auth.center.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {

        SysUser user = userMapper.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<SysPermission> permissionList = permissionMapper.findByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities =
                permissionList
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermCode()))
                .toList();

        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
