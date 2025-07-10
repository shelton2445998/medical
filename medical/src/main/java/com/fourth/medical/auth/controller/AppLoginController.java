package com.fourth.medical.auth.controller;

import com.fourth.medical.auth.annotation.Login;
import com.fourth.medical.auth.dto.AppAccountLoginDto;
import com.fourth.medical.auth.dto.AppLoginDto;
import com.fourth.medical.auth.service.AppLoginService;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.common.constant.LoginConstant;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.util.CookieUtil;
import com.fourth.medical.util.HttpServletRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "APP登录")
@RequestMapping("/app")
public class AppLoginController {

    @Autowired
    private AppLoginService appLoginService;

    /**
     * APP小程序登录
     *
     * @param appLoginDto
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @Operation(summary = "APP小程序登录")
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody AppLoginDto appLoginDto, HttpServletRequest request, HttpServletResponse response) {
        LoginTokenVo loginTokenVo = appLoginService.login(appLoginDto);
        // 输出token到cookie
        addCookie(loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * APP账号密码登录
     *
     * @param loginAccountAppDto
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/accountLogin")
    @Operation(summary = "APP账号密码登录")
    public ApiResult<LoginTokenVo> accountLogin(@Valid @RequestBody AppAccountLoginDto loginAccountAppDto, HttpServletRequest request, HttpServletResponse response) {
        LoginTokenVo loginTokenVo = appLoginService.accountLogin(loginAccountAppDto);
        // 输出token到cookie
        addCookie(loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 获取APP登录用户信息
     *
     * @return
     * @throws Exception
     */
    @Login
    @PostMapping("/getLoginUserInfo")
    @Operation(summary = "获取APP登录用户信息")
    public ApiResult<AppLoginVo> getLoginUserInfo() {
        AppLoginVo appLoginVo = appLoginService.getLoginUserInfo();
        return ApiResult.success(appLoginVo);
    }

    /**
     * APP退出
     *
     * @return
     * @throws Exception
     */
    @Login
    @PostMapping("/logout")
    @Operation(summary = "APP退出")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        appLoginService.logout();
        // 从cookie中删除token
        CookieUtil.deleteCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, request, response);
        return ApiResult.success();
    }

    /**
     * 输出token到cookie
     *
     * @param token
     * @param request
     * @param response
     */
    private void addCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        boolean docRequest = HttpServletRequestUtil.isDocRequest(request);
        if (docRequest) {
            CookieUtil.addCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, token, request, response);
        }
    }

}
