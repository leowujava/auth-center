package com.demo.auth.center.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author S00003829
 */
@Component
public class JwtUtil {

    // 建议放配置文件
    private final static String SECRET_KEY = "6eb83bea-dc2b-4977-9470-5c44a751c114";

    // 过期时间（1小时）
    private final static long EXPIRATION = 60 * 60 * 1000;

    // 生成token
    public static String generateToken(String username, Collection<String> authorities) {

        return Jwts.builder()
                .subject(username)
                .claim("auth", authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    // 解析token
    public static Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 是否过期
    public static boolean isExpired(String token) {

        Date exp = parseToken(token).getExpiration();
        return exp.before(new Date());
    }

    // 获取用户名
    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    // 获取权限
    public static List<String> getAuthorities(String token) {
        return parseToken(token).get("auth", List.class);
    }
}
