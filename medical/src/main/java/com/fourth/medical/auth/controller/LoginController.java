// 定义包名，指定当前类所在的包路径
package com.fourth.medical.auth.controller;

// 导入登录数据传输对象，用于接收登录请求参数
import com.fourth.medical.auth.dto.LoginDto;
// 导入登录服务接口，提供登录相关业务逻辑
import com.fourth.medical.auth.service.LoginService;
// 导入登录令牌视图对象，用于返回登录令牌信息
import com.fourth.medical.auth.vo.LoginTokenVo;
// 导入登录视图对象，用于返回登录用户信息
import com.fourth.medical.auth.vo.LoginVo;
// 导入登录常量类，定义登录相关的常量值
import com.fourth.medical.common.constant.LoginConstant;
// 导入框架响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入Cookie工具类，用于处理Cookie相关操作
import com.fourth.medical.util.CookieUtil;
// 导入Swagger操作注解，用于API文档生成
import io.swagger.v3.oas.annotations.Operation;
// 导入Swagger标签注解，用于API分组
import io.swagger.v3.oas.annotations.tags.Tag;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Web注解，用于REST API开发
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入Servlet HTTP响应接口
import javax.servlet.http.HttpServletResponse;
// 导入验证注解，用于参数校验
import javax.validation.Valid;

/**
 * 管理后台登录控制器
 * 提供管理后台的登录、获取用户信息、退出登录等功能
 * 处理管理员的身份认证和会话管理
 *
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "管理后台登录")  // Swagger文档标签，用于API分组
@RequestMapping("/admin")  // 设置控制器的基础请求路径
public class LoginController {

    // 注入登录服务，用于处理登录相关业务逻辑
    @Autowired
    private LoginService loginService;

    /**
     * 管理后台登录
     * 处理管理员的登录请求，验证用户名密码，生成登录令牌
     *
     * @param loginDto 登录数据传输对象，包含用户名和密码
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于设置Cookie
     * @return ApiResult<LoginTokenVo> 返回登录令牌的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/login")  // 处理POST请求，路径为/admin/login
    @Operation(summary = "管理后台登录")  // Swagger文档说明
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        // 调试输出：打印请求对象信息
        System.out.println("ABBBBBBBBBBBBBBBBBBBBBrequest: " + request);
        // 调试输出：打印响应对象信息
        System.out.println("CCCCCCCCCCCCCCCCCCCCCCresponse: " + response);
        // 调用登录服务进行身份验证，获取登录令牌
        LoginTokenVo loginTokenVo = loginService.login(loginDto);
        // 调试输出：打印登录令牌信息
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDloginTokenVo: " + loginTokenVo);
        // 将登录令牌添加到Cookie中，用于后续的身份验证
        CookieUtil.addCookie(LoginConstant.ADMIN_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        // 返回成功响应，包含登录令牌信息
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 获取管理后台登录用户信息
     * 获取当前登录管理员的详细信息，用于前端显示用户信息
     *
     * @return ApiResult<LoginVo> 返回登录用户信息的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @GetMapping("/info")  // 处理GET请求，路径为/admin/info
    @Operation(summary = "获取管理员信息")  // Swagger文档说明
    public ApiResult<LoginVo> getLoginUserInfo() {
        // 调用登录服务获取当前登录用户的详细信息
        LoginVo loginVo = loginService.getLoginUserInfo();
        // 返回成功响应，包含用户信息
        return ApiResult.success(loginVo);
    }

    /**
     * 管理后台退出
     * 处理管理员的退出登录请求，清除登录状态和缓存
     *
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于删除Cookie
     * @return ApiResult<Boolean> 返回退出结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/logout")  // 处理POST请求，路径为/admin/logout
    @Operation(summary = "管理后台退出")  // Swagger文档说明
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        // 调用登录服务清除登录缓存和会话信息
        loginService.logout();
        // 从Cookie中删除登录令牌，清除客户端的登录状态
        CookieUtil.deleteCookie(LoginConstant.ADMIN_COOKIE_TOKEN_NAME, request, response);
        // 返回成功响应
        return ApiResult.success();
    }

}
