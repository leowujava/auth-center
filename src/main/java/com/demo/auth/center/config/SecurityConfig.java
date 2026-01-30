package com.demo.auth.center.config;

import com.demo.auth.center.entity.model.SysPermission;
import com.demo.auth.center.filter.JwtFilter;
import com.demo.auth.center.mapper.PermissionMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @author S00003829
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private JwtFilter jwtFilter;

    @Autowired
    private PermissionMapper permissionMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // 读取数据库权限
        List<SysPermission> perms = permissionMapper.findAll();
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers("/login").permitAll();
                            // 动态RBAC配置
                            for (SysPermission p : perms) {
                                auth.requestMatchers(p.getUrl())
                                        .hasAuthority(p.getPermCode());
                            }
                            auth.anyRequest().denyAll();
                        }
                )
                // ⭐ 关键：加到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
