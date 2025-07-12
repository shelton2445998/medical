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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
@RestController
@Tag(name = "管理后台登录")
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 管理后台登录
     *
     * @param loginDto
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @Operation(summary = "管理后台登录")
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ABBBBBBBBBBBBBBBBBBBBBrequest: " + request);
        System.out.println("CCCCCCCCCCCCCCCCCCCCCCresponse: " + response);
        LoginTokenVo loginTokenVo = loginService.login(loginDto);
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDloginTokenVo: " + loginTokenVo);
        // 输出token到cookie
        CookieUtil.addCookie(LoginConstant.ADMIN_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 获取管理后台登录用户信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    @Operation(summary = "获取管理员信息")
    public ApiResult<LoginVo> getLoginUserInfo() {
        LoginVo loginVo = loginService.getLoginUserInfo();
        return ApiResult.success(loginVo);
    }

    /**
     * 管理后台退出
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/logout")
    @Operation(summary = "管理后台退出")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除缓存
        loginService.logout();
        // 从cookie中删除token
        CookieUtil.deleteCookie(LoginConstant.ADMIN_COOKIE_TOKEN_NAME, request, response);
        return ApiResult.success();
    }

}
