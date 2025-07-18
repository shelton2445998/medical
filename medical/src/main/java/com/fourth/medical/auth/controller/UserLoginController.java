// 定义包名，指定当前类所在的包路径
package com.fourth.medical.auth.controller;

// 导入登录DTO，用于接收用户登录和注册请求参数
import com.fourth.medical.auth.dto.LoginDto;
// 导入登录服务接口，提供用户登录和注册相关业务逻辑
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
import org.springframework.web.bind.annotation.*;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入Servlet HTTP响应接口
import javax.servlet.http.HttpServletResponse;
// 导入验证注解，用于参数校验
import javax.validation.Valid;

/**
 * 用户端登录控制器
 * 
 * 功能说明：
 * 提供用户端的登录、注册、退出登录等基础认证功能。
 * 主要用于普通用户的身份验证和会话管理。
 * 
 * 主要功能：
 * 1. 用户登录 - 用户名/密码登录验证
 * 2. 用户注册 - 新用户账号注册功能
 * 3. 退出登录 - 清除用户登录状态
 * 
 * 业务场景：
 * - 适用于管理后台的普通用户登录
 * - 与App端用户登录分离，使用不同的Token策略
 * - 与医生端登录分离，权限和功能不同
 * 
 * 认证方式：
 * - 用户名+密码认证
 * - Token-based会话管理
 * - Cookie存储Token信息
 * 
 * 安全特性：
 * - 密码加密存储
 * - Token过期管理
 * - 登录状态缓存
 * - 完整的操作日志
 * 
 * 技术实现：
 * - 复用通用登录服务（LoginService）
 * - 使用APP_COOKIE_TOKEN_NAME作为Token名称
 * - 支持Cookie和Header双重Token传递
 * 
 * 扩展说明：
 * 当前实现复用了通用登录服务，实际项目中可能需要：
 * - 创建专门的用户登录服务
 * - 实现用户特定的权限验证
 * - 添加用户角色管理功能
 * 
 * @author fourth
 * @date 2025-07-09
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j // 自动生成日志对象log
@RestController // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "用户登录") // Swagger文档标签，用于API分组
@RequestMapping("/user") // 设置控制器的基础请求路径
public class UserLoginController {

    /**
     * 登录服务
     * 提供用户登录、注册、退出登录等基础认证功能
     * 
     * 注意：当前复用通用登录服务，实际项目中建议创建专门的用户登录服务
     */
    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     * 
     * 功能说明：
     * 用户使用用户名和密码进行登录验证，验证成功后生成Token
     * 并设置到Cookie中，供后续请求使用。
     * 
     * 登录流程：
     * 1. 验证用户名和密码格式
     * 2. 检查用户账号是否存在
     * 3. 验证密码是否正确
     * 4. 检查用户账号状态（是否禁用）
     * 5. 生成Token并缓存登录信息
     * 6. 设置Cookie并返回Token信息
     * 
     * 认证方式：
     * - 用户名+密码认证
     * - 支持多种用户名格式（用户名、邮箱、手机号）
     * - 密码加密验证
     * 
     * 安全措施：
     * - 参数校验和格式验证
     * - 密码加密存储和比对
     * - 登录失败次数限制
     * - Token有效期控制
     * 
     * @param loginDto 登录数据传输对象，包含用户名和密码
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于设置Cookie
     * @return ApiResult<LoginTokenVo> 返回登录令牌的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/login") // 处理POST请求，路径为/user/login
    @Operation(summary = "用户登录") // Swagger文档说明
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        // 调用登录服务进行用户身份验证
        // 注意：这里复用登录服务，实际项目中可能需要专门的用户登录服务
        LoginTokenVo loginTokenVo = loginService.login(loginDto);
        
        // 将Token添加到Cookie中，使用APP_COOKIE_TOKEN_NAME作为Cookie名称
        // 这样可以与其他端（如医生端）的Token进行区分
        CookieUtil.addCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 用户注册
     * 
     * 功能说明：
     * 新用户账号注册功能，用户填写必要信息后创建新账号，
     * 注册成功后自动登录并返回Token。
     * 
     * 注册流程：
     * 1. 验证注册信息格式和完整性
     * 2. 检查用户名是否已存在
     * 3. 验证邮箱或手机号是否已被使用
     * 4. 创建新用户账号
     * 5. 初始化用户基本信息
     * 6. 自动登录并生成Token
     * 7. 设置Cookie并返回Token信息
     * 
     * 注册要求：
     * - 用户名唯一性验证
     * - 密码强度验证
     * - 邮箱或手机号验证
     * - 必要信息完整性检查
     * 
     * 安全措施：
     * - 参数校验和格式验证
     * - 密码加密存储
     * - 防止恶意注册
     * - 注册操作日志记录
     * 
     * 扩展功能：
     * - 可以添加邮箱验证
     * - 可以添加手机验证码
     * - 可以添加图形验证码
     * - 可以添加用户协议确认
     * 
     * @param loginDto 注册数据传输对象，包含用户名、密码等信息
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于设置Cookie
     * @return ApiResult<LoginTokenVo> 返回登录令牌的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/register") // 处理POST请求，路径为/user/register
    @Operation(summary = "用户注册") // Swagger文档说明
    public ApiResult<LoginTokenVo> register(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        // 调用登录服务进行用户注册
        // 注意：这里假设登录服务中有注册方法，实际可能需要创建专门的注册服务
        LoginTokenVo loginTokenVo = loginService.register(loginDto);
        
        // 注册成功后自动登录，将Token添加到Cookie中
        CookieUtil.addCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 用户退出登录
     * 
     * 功能说明：
     * 处理用户的退出登录请求，清除服务端的登录状态缓存
     * 和客户端的Cookie信息，确保用户安全退出。
     * 
     * 退出流程：
     * 1. 从Token中获取用户信息
     * 2. 清除Redis中的登录缓存
     * 3. 清除ThreadLocal中的登录信息
     * 4. 删除客户端Cookie中的Token
     * 5. 记录退出操作日志
     * 
     * 安全措施：
     * - 服务端Token立即失效
     * - 客户端Cookie完全清除
     * - 防止Token被恶意复用
     * - 完整的操作审计日志
     * 
     * 清理范围：
     * - Redis缓存中的用户会话信息
     * - ThreadLocal中的临时登录信息
     * - 客户端Cookie中的Token信息
     * - 相关的权限和菜单缓存
     * 
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于删除Cookie
     * @return ApiResult<Boolean> 返回退出结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/logout") // 处理POST请求，路径为/user/logout
    @Operation(summary = "用户退出") // Swagger文档说明
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        // 调用登录服务清除登录缓存和会话信息
        loginService.logout();
        
        // 从Cookie中删除Token，清除客户端的登录状态
        CookieUtil.deleteCookie(LoginConstant.APP_COOKIE_TOKEN_NAME, request, response);
        
        return ApiResult.success();
    }
} 