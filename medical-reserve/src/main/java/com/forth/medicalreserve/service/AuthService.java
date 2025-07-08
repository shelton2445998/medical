package com.forth.medicalreserve.service;

import com.forth.medicalreserve.dto.LoginRequest;
import com.forth.medicalreserve.dto.LoginResponse;
import com.forth.medicalreserve.dto.RegisterDTO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 登出
     * @param token 令牌
     */
    void logout(String token);
    
    /**
     * 验证令牌有效性
     * @param token 令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
    
    /**
     * 用户注册
     * @param registerDTO 注册请求
     * @return 用户ID
     */
    Integer register(RegisterDTO registerDTO);
} 