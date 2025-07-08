package com.forth.medicalreserve.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 */
@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "medical_reserve_secret_key";
    private static final Long EXPIRATION = 86400000L; // 24小时

    /**
     * 根据用户信息生成token
     * @param id 用户ID
     * @param username 用户名
     * @param userType 用户类型（用户/管理员/医生）
     * @return JWT token
     */
    public String generateToken(Integer id, String username, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        claims.put("userType", userType);
        return createToken(claims);
    }

    /**
     * 创建token
     * @param claims token中包含的信息
     * @return token
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 验证token是否有效
     * @param token token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否过期
     * @param token token
     * @return 是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 从token中获取过期时间
     * @param token token
     * @return 过期时间
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从token中获取用户ID
     * @param token token
     * @return 用户ID
     */
    public Integer getIdFromToken(String token) {
        return ((Number) getAllClaimsFromToken(token).get("id")).intValue();
    }

    /**
     * 从token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).get("username", String.class);
    }

    /**
     * 从token中获取用户类型
     * @param token token
     * @return 用户类型
     */
    public String getUserTypeFromToken(String token) {
        return getAllClaimsFromToken(token).get("userType", String.class);
    }

    /**
     * 从token中获取指定的信息
     * @param token token
     * @param claimsResolver 解析函数
     * @param <T> 返回类型
     * @return 解析结果
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中获取所有信息
     * @param token token
     * @return 所有信息
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
} 