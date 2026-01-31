package com.demo.auth.center.controller;


import com.demo.auth.center.entity.vo.ApiResponse;
import com.demo.auth.center.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wugaoyang
 * @date 2026/1/30 星期五
 *
 */
@RestController
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @RequestMapping("/login")
    public ApiResponse<?> login(String username, String password) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                );

        Authentication auth =
                authenticationManager.authenticate(token);
        User user = (User) auth.getPrincipal();

        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String jwt = JwtUtil.generateToken(
                user.getUsername(),
                authorities
        );

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("username", user.getUsername());

        return ApiResponse.success(result);
    }

    @RequestMapping("/user/info")
    public Object info() {
        return "info";
    }

    @PostMapping("/user/add")
    public Object add() {
        return "add";
    }
}
