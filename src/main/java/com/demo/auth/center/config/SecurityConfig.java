package com.demo.auth.center.config;

import com.demo.auth.center.entity.model.SysPermission;
import com.demo.auth.center.filter.JwtFilter;
import com.demo.auth.center.handler.LoginFailureHandler;
import com.demo.auth.center.handler.LoginSuccessHandler;
import com.demo.auth.center.mapper.PermissionMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author S00003829
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private JwtFilter jwtFilter;

//    @Autowired
//    private LoginFailureHandler loginFailureHandler;
//    @Autowired
//    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private WhiteListConfig whiteListConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // 读取数据库权限
        List<SysPermission> perms = permissionMapper.findAll();
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/login") // 必须和前端一致
//                        .successHandler(loginSuccessHandler)
//                        .failureHandler(loginFailureHandler) // 👈 核心
//                        .permitAll()
//                )
                .authorizeHttpRequests(auth -> {


                            List<String> whitelist = whiteListConfig.getWhitelist();
                            if (!CollectionUtils.isEmpty(whitelist)) {
                                whitelist.forEach(white -> {
                                    auth
                                            .requestMatchers(white).permitAll();
                                });
                            }
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
