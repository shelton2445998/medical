package com.fourth.medical.auth.controller;

import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.service.DoctorLoginService;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.constant.LoginConstant;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.service.SysMenuService;
import com.fourth.medical.system.vo.SysNavMenuTreeVo;
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
import java.util.List;

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
    
    @Autowired
    private SysMenuService sysMenuService;

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
        log.info("医生登录请求: {}", doctorLoginDto.getMobile());
        // 使用医生登录服务进行手机号+密码登录
        LoginTokenVo loginTokenVo = doctorLoginService.login(doctorLoginDto);
        log.info("医生登录成功: {}", loginTokenVo.getToken());
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
        log.info("开始执行getLoginUserInfo方法");
        try {
            // 添加直接从Redis获取登录信息的逻辑
            String token = TokenUtil.getToken();
            log.info("获取到的token: {}", token);
            
            if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
                log.info("token不为空，从Redis获取登录信息");
                LoginVo redisLoginVo = LoginUtil.getLoginVo(token); // 从Redis获取
                
                if (redisLoginVo != null) {
                    log.info("手动将登录信息设置到LoginCache: userId={}", redisLoginVo.getUserId());
                    com.fourth.medical.auth.cache.LoginCache.set(redisLoginVo); // 设置到线程本地变量
                }
            }
            
            log.info("准备获取医生信息");
            // 获取医生信息
            LoginVo loginVo = doctorLoginService.getLoginDoctorInfo();
            log.info("成功获取医生信息: userId={}, username={}", loginVo.getUserId(), loginVo.getUsername());
            return ApiResult.success(loginVo);
        } catch (Exception e) {
            log.error("获取医生信息发生异常", e);
            throw e;
        }
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
        log.info("医生退出登录");
        // 删除缓存
        doctorLoginService.logout();
        // 从cookie中删除token
        CookieUtil.deleteCookie(LoginConstant.DOCTOR_COOKIE_TOKEN_NAME, request, response);
        return ApiResult.success();
    }

    /**
     * 获取医生导航菜单
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/sysMenu/getNavMenuTreeList")
    @Operation(summary = "获取医生导航菜单")
    public ApiResult<List<SysNavMenuTreeVo>> getNavMenuTreeList() {
        log.info("开始执行医生getNavMenuTreeList方法");
        try {
            // 添加直接从Redis获取登录信息的逻辑
            String token = TokenUtil.getToken();
            log.info("获取到的token: {}", token);
            
            if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
                log.info("token不为空，从Redis获取登录信息");
                LoginVo redisLoginVo = LoginUtil.getLoginVo(token); // 从Redis获取
                
                if (redisLoginVo != null) {
                    log.info("手动将登录信息设置到LoginCache: userId={}", redisLoginVo.getUserId());
                    com.fourth.medical.auth.cache.LoginCache.set(redisLoginVo); // 设置到线程本地变量
                } else {
                    log.warn("从Redis获取的登录信息为null");
                }
            } else {
                log.warn("token为空，可能未登录");
            }
            
            log.info("调用sysMenuService.getNavMenuTreeList()获取菜单");
            // 获取导航菜单
            List<SysNavMenuTreeVo> list = sysMenuService.getNavMenuTreeList();
            log.info("获取菜单成功，菜单数量: {}", list != null ? list.size() : 0);
            
            if (list == null || list.isEmpty()) {
                log.warn("获取到的菜单为空，可能是权限问题");
            }
            
            return ApiResult.success(list);
        } catch (Exception e) {
            log.error("获取菜单信息发生异常", e);
            throw e;
        }
    }
} 