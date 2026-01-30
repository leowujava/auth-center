package com.demo.auth.center.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wugaoyang
 * @date 2026/1/30 星期五
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @PostMapping("/list")
    public Object list() {
        return "list";
    }
}
