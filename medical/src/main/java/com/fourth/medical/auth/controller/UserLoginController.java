package com.fourth.medical.auth.controller;

import com.fourth.medical.auth.dto.LoginDto;
import com.fourth.medical.auth.service.LoginService;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.constant.LoginConstant;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 用户端登录控制器
 *
 * @author fourth
 * @date 2025-07-09
 **/
@Slf4j
@RestController
@Tag(name = "用户登录")
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param loginDto
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        // 这里复用登录服务，实际项目中可能需要专门的用户登录服务
        LoginTokenVo loginTokenVo = loginService.login(loginDto);
        // 输出token到cookie
        CookieUtil.addCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 用户注册
     *
     * @param loginDto 包含用户名、密码等信息
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ApiResult<LoginTokenVo> register(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        // 这里假设登录服务中有注册方法，实际可能需要创建专门的注册服务
        LoginTokenVo loginTokenVo = loginService.register(loginDto);
        // 输出token到cookie
        CookieUtil.addCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 用户退出
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/logout")
    @Operation(summary = "用户退出")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除缓存
        loginService.logout();
        // 从cookie中删除token
        CookieUtil.deleteCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, request, response);
        return ApiResult.success();
    }

} 