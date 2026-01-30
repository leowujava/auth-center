package com.demo.auth.center.filter;

import com.demo.auth.center.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        // 登录接口直接跳过JWT校验
        return "/login".equals(path);
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null) {
            String token = header;
            if (header.startsWith("Bearer ")) {
                token = header.substring(7);
            }
            try {
                if (!JwtUtil.isExpired(token)) {
                    String username = JwtUtil.getUsername(token);
                    //@TODO 通过redis获取用户权限
                    List<String> auths = JwtUtil.getAuthorities(token);
                    List<SimpleGrantedAuthority> authorities = auths.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }

            } catch (Exception e) {
                // token非法直接忽略（会进入未登录状态）
            }
        }

        filterChain.doFilter(request, response);
    }
}
