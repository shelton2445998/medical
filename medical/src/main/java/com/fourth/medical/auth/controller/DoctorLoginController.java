package com.fourth.medical.auth.controller;

import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.service.DoctorLoginService;
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
 * 医生端登录控制器
 *
 * @author fourth
 * @date 2025-07-09
 **/
@Slf4j
@RestController
@Tag(name = "医生登录")
@RequestMapping("/doctor")
public class DoctorLoginController {

    @Autowired
    private DoctorLoginService doctorLoginService;

    /**
     * 医生登录
     *
     * @param doctorLoginDto
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @Operation(summary = "医生登录")
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody DoctorLoginDto doctorLoginDto, HttpServletRequest request, HttpServletResponse response) {
        // 使用医生登录服务进行手机号+密码登录
        LoginTokenVo loginTokenVo = doctorLoginService.login(doctorLoginDto);
        // 输出token到cookie
        CookieUtil.addCookie(LoginConstant.DOCTOR_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 获取医生信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    @Operation(summary = "获取医生信息")
    public ApiResult<LoginVo> getLoginUserInfo() {
        // 获取医生信息
        LoginVo loginVo = doctorLoginService.getLoginDoctorInfo();
        return ApiResult.success(loginVo);
    }

    /**
     * 医生退出
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/logout")
    @Operation(summary = "医生退出")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除缓存
        doctorLoginService.logout();
        // 从cookie中删除token
        CookieUtil.deleteCookie(LoginConstant.DOCTOR_COOKIE_TOKEN_NAME, request, response);
        return ApiResult.success();
    }

} 