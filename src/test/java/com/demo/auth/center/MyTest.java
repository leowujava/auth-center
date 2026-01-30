package com.demo.auth.center;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/**
 * @author wugaoyang
 * @date 2026/1/30 星期五
 *
 */
public class MyTest {

    @Test
    public void test(){
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);
    }

    @Test
    public void test1(){
        String string = UUID.randomUUID().toString();
        System.out.println(string);
    }
}
