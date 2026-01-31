package com.demo.auth.center.handler;

import com.demo.auth.center.entity.vo.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String msg = "用户名或密码错误";

        if (exception instanceof LockedException) {
            msg = "账号已被锁定";
        } else if (exception instanceof DisabledException) {
            msg = "账号已被禁用";
        } else if (exception instanceof AccountExpiredException) {
            msg = "账号已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            msg = "密码已过期";
        }

        ApiResponse<Void> result =
                ApiResponse.error(401, msg);

        response.getWriter().write(
                new ObjectMapper().writeValueAsString(result)
        );
    }
}
