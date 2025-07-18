// 定义包名，指定当前类所在的包路径
package com.fourth.medical.user.controller;

// 导入框架响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入系统用户密码更新DTO，用于接收密码修改请求参数
import com.fourth.medical.system.dto.SysUserUpdatePasswordDto;
// 导入系统用户资料更新DTO，用于接收个人资料修改请求参数
import com.fourth.medical.system.dto.SysUserUpdateProfileDto;
// 导入系统用户服务接口，提供用户相关业务逻辑
import com.fourth.medical.system.service.SysUserService;
// 导入系统用户视图对象，用于返回用户信息
import com.fourth.medical.system.vo.SysUserVo;
// 导入登录工具类，用于获取当前登录用户信息
import com.fourth.medical.auth.util.LoginUtil;
// 导入业务异常类，用于抛出业务逻辑异常
import com.fourth.medical.framework.exception.BusinessException;
// 导入App用户资料更新DTO，用于接收App用户资料修改请求参数
import com.fourth.medical.user.dto.AppUserUpdateProfileDto;
// 导入用户服务接口，提供App用户相关业务逻辑
import com.fourth.medical.user.service.UserService;
// 导入App用户视图对象，用于返回App用户信息
import com.fourth.medical.user.vo.AppUserVo;
// 导入App登录工具类，用于获取当前App登录用户信息
import com.fourth.medical.auth.util.AppLoginUtil;
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

// 导入验证注解，用于参数校验
import javax.validation.Valid;

/**
 * App用户控制器
 * 
 * 功能说明：
 * 提供App端用户相关的API接口，包括用户信息查询、修改、密码管理等功能。
 * 该控制器区分了系统用户（SysUser）和App用户（User）两种不同的用户类型。
 * 
 * 用户类型说明：
 * 1. 系统用户（SysUser）- 主要用于管理后台的用户
 * 2. App用户（User）- 主要用于移动端应用的用户
 * 
 * 主要功能：
 * 1. 获取用户信息 - 支持系统用户和App用户信息获取
 * 2. 修改用户资料 - 支持个人资料信息更新
 * 3. 修改用户密码 - 支持密码安全修改
 * 4. App用户管理 - 专门针对App用户的管理功能
 * 
 * 业务流程：
 * 1. 用户登录后可以查看自己的个人信息
 * 2. 用户可以修改个人资料（姓名、邮箱、手机号等）
 * 3. 用户可以修改登录密码（需要验证旧密码）
 * 4. App用户有独立的资料管理功能
 * 
 * 权限控制：
 * - 用户只能查看和修改自己的信息
 * - 需要登录Token验证
 * - 密码修改需要验证旧密码
 * - 支持不同用户类型的权限隔离
 * 
 * 数据安全：
 * - 敏感信息（如密码）不会在响应中返回
 * - 密码修改采用加密存储
 * - 参数校验确保数据完整性
 * - 操作日志记录用户行为
 * 
 * 技术特点：
 * - 支持系统用户和App用户双重管理
 * - 使用不同的登录工具类获取用户信息
 * - 完整的异常处理和错误响应
 * - 支持参数校验和数据验证
 * 
 * API路径说明：
 * - /user/info - 获取系统用户信息
 * - /user/update - 修改系统用户资料
 * - /user/password - 修改系统用户密码
 * - /user/app/info - 获取App用户信息
 * - /user/app/update - 修改App用户资料
 * 
 * @author geekidea
 * @date 2022/7/9
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "App用户")  // Swagger文档标签，用于API分组
@RequestMapping("/user")  // 设置控制器的基础请求路径
public class AppUserController {

    /**
     * 系统用户服务
     * 用于处理系统用户（SysUser）相关的业务逻辑
     */
    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户服务
     * 用于处理App用户（User）相关的业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * 获取系统用户信息
     * 
     * 功能说明：
     * 根据当前登录用户的ID获取系统用户的详细信息。
     * 主要用于管理后台用户的个人信息展示。
     * 
     * 获取流程：
     * 1. 从当前登录上下文中获取用户ID
     * 2. 验证用户ID的有效性
     * 3. 查询系统用户的详细信息
     * 4. 返回用户信息（过滤敏感数据）
     * 
     * 数据内容：
     * - 用户基本信息（姓名、用户名、邮箱等）
     * - 用户状态信息（是否启用、创建时间等）
     * - 角色和权限信息
     * - 部门和组织信息
     * 
     * 权限控制：
     * - 需要有效的登录Token
     * - 只能查看自己的个人信息
     * - 敏感信息（如密码）不会返回
     * 
     * @return ApiResult<SysUserVo> 返回系统用户信息的统一响应格式
     * @throws BusinessException 当用户ID为空时抛出业务异常
     * @throws Exception 可能抛出的其他异常
     */
    @GetMapping("/info")  // 处理GET请求，路径为/user/info
    @Operation(summary = "获取用户信息")  // Swagger文档说明
    public ApiResult<SysUserVo> getUserInfo() {
        // 从登录上下文中获取当前登录用户的ID
        Long userId = LoginUtil.getUserId();
        
        // 验证用户ID是否有效，如果为空则抛出业务异常
        if (userId == null) {
            throw new BusinessException("用户ID为空");
        }
        
        // 调用服务层根据用户ID获取用户详细信息
        SysUserVo sysUserVo = sysUserService.getSysUserById(userId);
        
        // 返回成功响应，包含用户信息
        return ApiResult.success(sysUserVo);
    }

    /**
     * 修改系统用户个人资料
     * 
     * 功能说明：
     * 更新当前登录用户的个人资料信息，包括姓名、邮箱、
     * 手机号等基本信息。
     * 
     * 修改流程：
     * 1. 验证请求参数的有效性
     * 2. 从登录上下文中获取当前用户ID
     * 3. 验证用户权限（只能修改自己的资料）
     * 4. 更新用户资料信息
     * 5. 返回操作结果
     * 
     * 可修改内容：
     * - 用户姓名
     * - 邮箱地址
     * - 手机号码
     * - 个人简介
     * - 头像信息
     * 
     * 数据验证：
     * - 邮箱格式验证
     * - 手机号格式验证
     * - 必填字段验证
     * - 数据长度验证
     * 
     * 安全措施：
     * - 权限验证确保只能修改自己的资料
     * - 参数校验防止恶意数据
     * - 操作日志记录修改行为
     * 
     * @param dto 用户资料更新DTO，包含要修改的用户信息
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PutMapping("/update")  // 处理PUT请求，路径为/user/update
    @Operation(summary = "修改用户个人资料")  // Swagger文档说明
    public ApiResult updateUserInfo(@Valid @RequestBody SysUserUpdateProfileDto dto) {
        // 调用服务层方法更新用户资料，@Valid注解用于参数校验
        boolean flag = sysUserService.updateProfile(dto);
        
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }

    /**
     * 修改系统用户密码
     * 
     * 功能说明：
     * 更新当前登录用户的登录密码，需要验证旧密码
     * 确保密码修改的安全性。
     * 
     * 修改流程：
     * 1. 验证请求参数的有效性
     * 2. 从登录上下文中获取当前用户ID
     * 3. 验证旧密码的正确性
     * 4. 验证新密码的强度要求
     * 5. 更新密码并加密存储
     * 6. 返回操作结果
     * 
     * 安全要求：
     * - 必须提供正确的旧密码
     * - 新密码需要满足强度要求
     * - 密码加密存储
     * - 操作日志记录
     * 
     * 密码强度要求：
     * - 最小长度限制
     * - 包含大小写字母、数字、特殊字符
     * - 不能与历史密码相同
     * - 不能包含常见弱密码
     * 
     * 后续处理：
     * - 密码修改后可能需要重新登录
     * - 清除其他设备的登录状态
     * - 发送密码修改通知
     * 
     * @param dto 密码更新DTO，包含旧密码和新密码
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PutMapping("/password")  // 处理PUT请求，路径为/user/password
    @Operation(summary = "修改用户密码")  // Swagger文档说明
    public ApiResult updatePassword(@Valid @RequestBody SysUserUpdatePasswordDto dto) {
        // 调用服务层方法更新用户密码，@Valid注解用于参数校验
        boolean flag = sysUserService.updatePassword(dto);
        
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }

    /**
     * 获取App用户信息
     * 
     * 功能说明：
     * 根据当前App登录用户的ID获取App用户的详细信息。
     * 主要用于移动端应用的用户个人信息展示。
     * 
     * 与系统用户的区别：
     * - 使用AppLoginUtil获取用户ID
     * - 查询User表而非SysUser表
     * - 返回AppUserVo而非SysUserVo
     * - 包含App用户特有的字段和信息
     * 
     * 获取流程：
     * 1. 从App登录上下文中获取用户ID
     * 2. 验证用户ID的有效性
     * 3. 查询App用户的详细信息
     * 4. 返回App用户信息
     * 
     * 数据内容：
     * - App用户基本信息（昵称、头像、手机号等）
     * - 用户状态信息（是否认证、注册时间等）
     * - 健康档案信息
     * - 家庭成员信息
     * - 体检记录统计
     * 
     * @return ApiResult<AppUserVo> 返回App用户信息的统一响应格式
     * @throws BusinessException 当用户ID为空时抛出业务异常
     * @throws Exception 可能抛出的其他异常
     */
    @GetMapping("/app/info")  // 处理GET请求，路径为/user/app/info
    @Operation(summary = "获取App用户信息")  // Swagger文档说明
    public ApiResult<AppUserVo> getAppUserInfo() {
        // 从App登录上下文中获取当前登录用户的ID
        Long userId = AppLoginUtil.getUserId();
        
        // 验证用户ID是否有效，如果为空则抛出业务异常
        if (userId == null) {
            throw new BusinessException("用户ID为空");
        }
        
        // 获取App用户的详细信息
        AppUserVo appUserVo = userService.getProfile();
        
        // 返回成功响应，包含App用户信息
        return ApiResult.success(appUserVo);
    }

    /**
     * 修改App用户个人资料
     * 
     * 功能说明：
     * 更新当前App登录用户的个人资料信息，包括昵称、头像、
     * 健康信息等App用户特有的信息。
     * 
     * 与系统用户资料修改的区别：
     * - 使用AppUserUpdateProfileDto而非SysUserUpdateProfileDto
     * - 更新User表而非SysUser表
     * - 包含App用户特有的字段
     * - 支持健康档案信息更新
     * 
     * 修改流程：
     * 1. 验证请求参数的有效性
     * 2. 从App登录上下文中获取当前用户ID
     * 3. 验证用户权限（只能修改自己的资料）
     * 4. 更新App用户资料信息
     * 5. 返回操作结果
     * 
     * 可修改内容：
     * - 用户昵称
     * - 头像信息
     * - 性别和年龄
     * - 身高体重等健康信息
     * - 过敏史和病史信息
     * - 紧急联系人信息
     * 
     * 数据验证：
     * - 昵称长度和格式验证
     * - 健康数据范围验证
     * - 必填字段验证
     * - 数据类型验证
     * 
     * @param dto App用户资料更新DTO，包含要修改的用户信息
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/app/update")  // 处理POST请求，路径为/user/app/update
    @Operation(summary = "修改App用户个人资料")  // Swagger文档说明
    public ApiResult updateAppUserInfo(@Valid @RequestBody AppUserUpdateProfileDto dto) {
        // 调用服务层方法更新App用户资料，@Valid注解用于参数校验
        boolean flag = userService.updateAppUserProfile(dto);
        
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }
}
