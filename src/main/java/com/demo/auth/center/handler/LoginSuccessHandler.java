package com.demo.auth.center.handler;

import com.demo.auth.center.entity.vo.ApiResponse;
import com.demo.auth.center.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // 1. 获取用户名
        String username = authentication.getName();
        User user = (User) authentication.getPrincipal();

        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 2. 生成 JWT
        String token = JwtUtil.generateToken(username, authorities);
        // 3. 返回给前端
        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        ApiResponse<Map<String, String>> result =
                ApiResponse.success(data);

        response.getWriter().write(
                new ObjectMapper().writeValueAsString(result)
        );
    }
}
