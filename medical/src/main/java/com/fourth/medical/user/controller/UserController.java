// 定义包名，指定当前类所在的包路径
package com.fourth.medical.user.controller;

// 导入权限注解，用于控制接口访问权限
import com.fourth.medical.auth.annotation.Permission;
// 导入系统日志类型枚举，用于记录操作日志
import com.fourth.medical.common.enums.SysLogType;
// 导入日志注解，用于自动记录操作日志
import com.fourth.medical.framework.annotation.Log;
// 导入分页工具类，用于处理分页查询
import com.fourth.medical.framework.page.Paging;
// 导入框架响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入用户DTO，用于接收用户数据
import com.fourth.medical.user.dto.UserDto;
// 导入用户查询对象，用于接收查询条件
import com.fourth.medical.user.query.UserQuery;
// 导入用户服务接口，提供用户相关业务逻辑
import com.fourth.medical.user.service.UserService;
// 导入用户视图对象，用于返回用户信息
import com.fourth.medical.user.vo.UserVo;
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
 * 用户信息管理控制器
 * 提供用户信息的增删改查功能，包括添加、修改、删除、查询用户信息等操作
 * 主要用于后台管理系统的用户管理功能
 *
 * @author geekidea
 * @since 2023-11-30
 */
@Slf4j  // 自动生成日志对象log
@RestController  // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "用户信息")  // Swagger文档标签，用于API分组
@RequestMapping("/admin/user")  // 设置控制器的基础请求路径，用于后台管理
public class UserController {

    // 注入用户服务，用于处理用户相关业务逻辑
    @Autowired
    private UserService userService;

    /**
     * 添加用户信息
     * 创建新的用户记录，包含用户基本信息的录入
     *
     * @param userDto 用户数据传输对象，包含要添加的用户信息
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @Log(value = "添加用户信息", type = SysLogType.ADD)  // 记录添加用户的操作日志
    @PostMapping("/addUser")  // 处理POST请求，路径为/admin/user/addUser
    @Operation(summary = "添加用户信息")  // Swagger文档说明
    @Permission("user:add")  // 权限控制，需要user:add权限
    public ApiResult addUser(@Valid @RequestBody UserDto userDto) {
        // 调用服务层方法添加用户，@Valid注解用于参数校验
        boolean flag = userService.addUser(userDto);
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }

    /**
     * 修改用户信息
     * 更新现有用户的基本信息，包括个人资料、联系方式等
     *
     * @param userDto 用户数据传输对象，包含要修改的用户信息
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @Log(value = "修改用户信息", type = SysLogType.UPDATE)  // 记录修改用户的操作日志
    @PostMapping("/updateUser")  // 处理POST请求，路径为/admin/user/updateUser
    @Operation(summary = "修改用户信息")  // Swagger文档说明
    @Permission("user:update")  // 权限控制，需要user:update权限
    public ApiResult updateUser(@Valid @RequestBody UserDto userDto) {
        // 调用服务层方法更新用户信息，@Valid注解用于参数校验
        boolean flag = userService.updateUser(userDto);
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }

    /**
     * 删除用户信息
     * 根据用户ID删除指定的用户记录，执行逻辑删除操作
     *
     * @param id 要删除的用户ID
     * @return ApiResult 返回操作结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @Log(value = "删除用户信息", type = SysLogType.DELETE)  // 记录删除用户的操作日志
    @PostMapping("/deleteUser/{id}")  // 处理POST请求，路径为/admin/user/deleteUser/{id}
    @Operation(summary = "删除用户信息")  // Swagger文档说明
    @Permission("user:delete")  // 权限控制，需要user:delete权限
    public ApiResult deleteUser(@PathVariable Long id) {
        // 调用服务层方法删除用户，@PathVariable注解用于获取路径参数
        boolean flag = userService.deleteUser(id);
        // 根据操作结果返回相应的响应
        return ApiResult.result(flag);
    }

    /**
     * 获取用户信息详情
     * 根据用户ID查询用户的详细信息，包括基本资料、联系方式等
     *
     * @param id 要查询的用户ID
     * @return ApiResult<UserVo> 返回用户详细信息的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/getUser/{id}")  // 处理POST请求，路径为/admin/user/getUser/{id}
    @Operation(summary = "用户信息详情")  // Swagger文档说明
    @Permission("user:info")  // 权限控制，需要user:info权限
    public ApiResult<UserVo> getUser(@PathVariable Long id) {
        // 调用服务层方法根据ID获取用户详细信息，@PathVariable注解用于获取路径参数
        UserVo userVo = userService.getUserById(id);
        // 返回成功响应，包含用户详细信息
        return ApiResult.success(userVo);
    }

    /**
     * 用户信息分页列表
     * 根据查询条件分页获取用户列表，支持条件筛选和分页显示
     *
     * @param query 用户查询对象，包含查询条件和分页参数
     * @return ApiResult<UserVo> 返回分页用户列表的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/getUserPage")  // 处理POST请求，路径为/admin/user/getUserPage
    @Operation(summary = "用户信息分页列表")  // Swagger文档说明
    @Permission("user:page")  // 权限控制，需要user:page权限
    public ApiResult<UserVo> getUserPage(@Valid @RequestBody UserQuery query) {
        // 调用服务层方法获取分页用户列表，@Valid注解用于参数校验
        Paging<UserVo> paging = userService.getUserPage(query);
        // 返回成功响应，包含分页用户列表
        return ApiResult.success(paging);
    }

}
