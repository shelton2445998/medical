package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.common.Result;
import com.forth.medicalreserve.dto.LoginRequest;
import com.forth.medicalreserve.dto.LoginResponse;
import com.forth.medicalreserve.dto.RegisterDTO;
import com.forth.medicalreserve.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return Result.success(loginResponse);
    }

    /**
     * 用户注册
     * @param registerDTO 注册请求
     * @return 用户ID
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Integer> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Integer userId = authService.register(registerDTO);
        return Result.success(userId);
    }

    /**
     * 登出
     * @param token 令牌
     * @return 登出结果
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        // 从Authorization头中提取JWT令牌
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        authService.logout(token);
        return Result.success();
    }

    /**
     * 验证令牌有效性
     * @param token 令牌
     * @return 验证结果
     */
    @ApiOperation("验证令牌有效性")
    @GetMapping("/validate")
    public Result<Boolean> validateToken(@RequestParam String token) {
        boolean valid = authService.validateToken(token);
        return Result.success(valid);
    }
} 