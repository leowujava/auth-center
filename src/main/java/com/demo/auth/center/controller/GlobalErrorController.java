package com.demo.auth.center.controller;

import com.demo.auth.center.entity.vo.ApiResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public ApiResponse<Void> handleError(HttpServletRequest request) {

        Integer statusCode =
                (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode == null) {
            return ApiResponse.error(500, "系统异常");
        }

        if (statusCode == 404) {
            return ApiResponse.error(404, "接口不存在");
        }

        if (statusCode == 403) {
            return ApiResponse.error(403, "无权限访问");
        }

        return ApiResponse.error(statusCode, "系统异常");
    }
}
