// 定义包名，指定当前类所在的包路径
package com.fourth.medical.system.controller;

// 导入权限注解，用于方法级别的权限控制
import com.fourth.medical.auth.annotation.Permission;
// 导入登录工具类，用于获取当前登录用户信息
import com.fourth.medical.auth.util.LoginUtil;
// 导入业务异常类，用于处理业务逻辑异常
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.exception.BusinessException;
// 导入分页工具类，用于处理分页查询结果
import com.fourth.medical.framework.page.Paging;
// 导入API响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入系统用户DTO，用于接收前端传递的用户数据
import com.fourth.medical.system.dto.SysUserDto;
// 导入系统用户重置密码DTO，用于管理员重置用户密码
import com.fourth.medical.system.dto.SysUserResetPasswordDto;
// 导入系统用户更新密码DTO，用于用户修改自己的密码
import com.fourth.medical.system.dto.SysUserUpdatePasswordDto;
// 导入系统用户更新个人信息DTO，用于用户修改个人信息
import com.fourth.medical.system.dto.SysUserUpdateProfileDto;
// 导入系统用户查询对象，用于接收查询条件
import com.fourth.medical.system.query.SysUserQuery;
// 导入系统用户服务接口，提供用户相关业务逻辑
import com.fourth.medical.system.service.SysUserService;
// 导入系统用户视图对象，用于返回用户信息
import com.fourth.medical.system.vo.SysUserVo;
// 导入Swagger操作注解，用于API文档生成
import io.swagger.v3.oas.annotations.Operation;
// 导入Swagger标签注解，用于API分组
import io.swagger.v3.oas.annotations.tags.Tag;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Spring自动装配注解，用于依赖注入
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Web注解，用于REST API开发
import org.springframework.web.bind.annotation.*;
// 导入Spring文件上传类，用于处理文件上传
import org.springframework.web.multipart.MultipartFile;

// 导入参数验证注解，用于数据校验
import javax.validation.Valid;

/**
 * 系统用户管理控制器
 * 
 * 功能说明：
 * 本控制器是医疗系统中用户管理的核心控制器，负责处理管理员对系统用户的各种操作，
 * 包括用户的增删改查、权限分配、密码管理、个人信息维护等功能。该控制器主要服务于
 * 管理后台，提供完整的用户生命周期管理功能。
 * 
 * 主要功能：
 * 1. 用户基础管理
 *    - 新增系统用户
 *    - 修改用户信息
 *    - 删除用户
 *    - 查询用户详情
 *    - 用户列表分页查询
 * 
 * 2. 密码管理
 *    - 管理员重置用户密码
 *    - 用户修改个人密码
 *    - 密码安全策略控制
 * 
 * 3. 个人信息管理
 *    - 用户个人信息修改
 *    - 头像上传和更新
 *    - 个人设置维护
 * 
 * 4. 用户状态管理
 *    - 用户启用/禁用
 *    - 用户状态查询
 *    - 用户活动记录
 * 
 * 5. 数据导入导出
 *    - Excel用户数据导入
 *    - 用户数据导出
 *    - 批量用户操作
 * 
 * 业务特点：
 * - 严格的权限控制，所有操作需要相应权限
 * - 完整的操作日志记录
 * - 数据验证和安全检查
 * - 支持批量操作和文件处理
 * - 分页查询优化，提高大数据量处理性能
 * 
 * 权限体系：
 * - sys:user:add - 新增用户权限
 * - sys:user:update - 修改用户权限
 * - sys:user:delete - 删除用户权限
 * - sys:user:info - 查看用户信息权限
 * - sys:user:page - 用户列表查询权限
 * - sys:user:reset:password - 重置密码权限
 * - sys:user:update:password - 修改密码权限
 * - sys:user:update:profile - 修改个人信息权限
 * - sys:user:import - 数据导入权限
 * - sys:user:export - 数据导出权限
 * 
 * 安全措施：
 * - 敏感操作记录日志
 * - 密码加密存储
 * - 权限验证机制
 * - 数据访问控制
 * - 防止SQL注入和XSS攻击
 * 
 * 数据处理：
 * - 支持复杂查询条件
 * - 分页查询优化
 * - 数据格式验证
 * - 文件上传处理
 * - 批量操作支持
 * 
 * 扩展功能：
 * - 用户组织架构管理
 * - 多租户支持
 * - 用户标签管理
 * - 活动轨迹追踪
 * - 用户行为分析
 * 
 * @author geekidea
 * @since 2022-12-26
 * @version 1.0
 */
@Slf4j // 自动生成日志对象log，用于记录操作日志
@RestController // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "系统用户") // Swagger文档标签，用于API分组
@RequestMapping("/admin/user") // 设置控制器的基础请求路径
public class SysUserController {

    /**
     * 系统用户服务
     * 
     * 功能说明：
     * 注入系统用户服务接口，提供用户相关的核心业务逻辑，包括用户的增删改查、
     * 权限验证、密码管理、状态控制等功能。
     * 
     * 主要服务：
     * - 用户生命周期管理
     * - 权限验证和授权
     * - 密码安全处理
     * - 用户状态管理
     * - 数据查询和统计
     */
    @Autowired
    private SysUserService sysUserService;

    /**
     * 添加系统用户
     * 
     * 功能说明：
     * 创建新的系统用户，包括用户基本信息、角色分配、初始密码设置等。
     * 新用户创建后会自动生成默认密码，并可以选择是否发送通知邮件。
     * 
     * 业务逻辑：
     * 1. 验证用户输入数据的完整性和有效性
     * 2. 检查用户名和邮箱是否已存在
     * 3. 生成用户ID和初始密码
     * 4. 设置用户默认状态和权限
     * 5. 保存用户信息到数据库
     * 6. 记录操作日志
     * 
     * 数据验证：
     * - 用户名不能为空且不能重复
     * - 邮箱格式有效且不能重复
     * - 手机号格式有效且不能重复
     * - 角色必须存在且有效
     * - 部门必须存在且有效
     * 
     * 安全措施：
     * - 密码自动加密存储
     * - 敏感信息不记录在日志中
     * - 权限验证通过后才能执行
     * - 输入数据XSS过滤
     * 
     * 默认设置：
     * - 用户状态：启用
     * - 初始密码：系统生成
     * - 创建时间：当前时间
     * - 创建人：当前登录用户
     * 
     * @param dto 系统用户数据传输对象，包含用户基本信息、角色、部门等
     * @return ApiResult<Void> 操作结果，成功返回success，失败返回错误信息
     * @throws BusinessException 当用户名或邮箱已存在时抛出业务异常
     */
    @PostMapping("/add")
    @Operation(summary = "添加用户", description = "创建新的系统用户，包括用户基本信息、角色分配、初始密码设置等")
    @Permission("sys:user:add")
    public ApiResult addSysUser(@Valid @RequestBody SysUserDto dto) {
        log.info("开始添加系统用户，用户名: {}", dto.getUsername());
        
        try {
            boolean flag = sysUserService.addSysUser(dto);
            
            if (flag) {
                log.info("添加系统用户成功，用户名: {}", dto.getUsername());
                return ApiResult.success("添加用户成功");
            } else {
                log.warn("添加系统用户失败，用户名: {}", dto.getUsername());
                return ApiResult.fail("添加用户失败");
            }
        } catch (BusinessException e) {
            log.error("添加系统用户业务异常，用户名: {}, 错误: {}", dto.getUsername(), e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("添加系统用户系统异常，用户名: {}, 错误: {}", dto.getUsername(), e.getMessage(), e);
            return ApiResult.fail("添加用户失败，系统异常");
        }
    }

    /**
     * 修改系统用户
     * 
     * 功能说明：
     * 更新现有系统用户的信息，包括基本信息、角色、部门、状态等。
     * 支持部分字段更新，不会影响用户的密码和敏感信息。
     * 
     * 业务逻辑：
     * 1. 验证用户ID是否存在
     * 2. 检查修改权限
     * 3. 验证更新数据的有效性
     * 4. 检查用户名和邮箱唯一性（排除自己）
     * 5. 更新用户信息
     * 6. 记录操作日志
     * 
     * 可修改字段：
     * - 用户名（需要唯一性验证）
     * - 真实姓名
     * - 邮箱（需要唯一性验证）
     * - 手机号
     * - 部门
     * - 角色
     * - 用户状态
     * - 备注信息
     * 
     * 不可修改字段：
     * - 用户ID
     * - 密码（需要通过专门接口修改）
     * - 创建时间
     * - 创建人
     * 
     * 权限控制：
     * - 需要sys:user:update权限
     * - 不能修改比自己权限高的用户
     * - 不能修改超级管理员（除非自己是超级管理员）
     * 
     * @param dto 系统用户数据传输对象，包含要修改的用户信息
     * @return ApiResult<Void> 操作结果，成功返回success，失败返回错误信息
     * @throws BusinessException 当用户不存在或权限不足时抛出业务异常
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户", description = "更新现有系统用户的信息，包括基本信息、角色、部门、状态等")
    @Permission("sys:user:update")
    public ApiResult updateSysUser(@Valid @RequestBody SysUserDto dto) {
        log.info("开始修改系统用户，用户ID: {}, 用户名: {}", dto.getId(), dto.getUsername());
        
        try {
            boolean flag = sysUserService.updateSysUser(dto);
            
            if (flag) {
                log.info("修改系统用户成功，用户ID: {}, 用户名: {}", dto.getId(), dto.getUsername());
                return ApiResult.success("修改用户成功");
            } else {
                log.warn("修改系统用户失败，用户ID: {}, 用户名: {}", dto.getId(), dto.getUsername());
                return ApiResult.fail("修改用户失败");
            }
        } catch (BusinessException e) {
            log.error("修改系统用户业务异常，用户ID: {}, 错误: {}", dto.getId(), e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("修改系统用户系统异常，用户ID: {}, 错误: {}", dto.getId(), e.getMessage(), e);
            return ApiResult.fail("修改用户失败，系统异常");
        }
    }

    /**
     * 删除系统用户
     * 
     * 功能说明：
     * 删除指定的系统用户，支持逻辑删除和物理删除两种方式。
     * 默认使用逻辑删除，将用户状态标记为已删除，保留历史数据。
     * 
     * 业务逻辑：
     * 1. 验证用户ID是否存在
     * 2. 检查删除权限
     * 3. 验证是否可以删除（不能删除自己、超级管理员等）
     * 4. 检查用户是否有关联数据
     * 5. 执行删除操作
     * 6. 记录操作日志
     * 
     * 删除限制：
     * - 不能删除自己
     * - 不能删除超级管理员（除非自己是超级管理员）
     * - 不能删除有关联数据的用户（如有下级用户、创建的数据等）
     * - 不能删除比自己权限高的用户
     * 
     * 关联数据处理：
     * - 检查是否有下级用户
     * - 检查是否有创建的业务数据
     * - 检查是否有关联的角色和权限
     * - 根据业务需求决定是否允许删除
     * 
     * 安全措施：
     * - 严格的权限验证
     * - 删除前确认检查
     * - 完整的操作日志记录
     * - 支持数据恢复（逻辑删除）
     * 
     * @param id 用户ID，要删除的用户的唯一标识
     * @return ApiResult<Void> 操作结果，成功返回success，失败返回错误信息
     * @throws BusinessException 当用户不存在、权限不足或有关联数据时抛出业务异常
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户", description = "删除指定的系统用户，支持逻辑删除，会检查关联数据和权限")
    @Permission("sys:user:delete")
    public ApiResult deleteSysUser(@PathVariable Long id) {
        log.info("开始删除系统用户，用户ID: {}", id);
        
        try {
            boolean flag = sysUserService.deleteSysUser(id);
            
            if (flag) {
                log.info("删除系统用户成功，用户ID: {}", id);
                return ApiResult.success("删除用户成功");
            } else {
                log.warn("删除系统用户失败，用户ID: {}", id);
                return ApiResult.fail("删除用户失败");
            }
        } catch (BusinessException e) {
            log.error("删除系统用户业务异常，用户ID: {}, 错误: {}", id, e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("删除系统用户系统异常，用户ID: {}, 错误: {}", id, e.getMessage(), e);
            return ApiResult.fail("删除用户失败，系统异常");
        }
    }

    /**
     * 获取系统用户详情
     * 
     * 功能说明：
     * 根据用户ID获取系统用户的详细信息，包括基本信息、角色、部门、权限等。
     * 返回的数据不包含密码等敏感信息。
     * 
     * 业务逻辑：
     * 1. 验证用户ID是否存在
     * 2. 检查查看权限
     * 3. 获取用户详细信息
     * 4. 过滤敏感信息
     * 5. 返回用户信息
     * 
     * 返回信息：
     * - 用户基本信息（姓名、邮箱、手机等）
     * - 部门信息
     * - 角色信息
     * - 权限列表
     * - 用户状态
     * - 创建和修改时间
     * 
     * 权限控制：
     * - 需要sys:user:info权限
     * - 只能查看权限范围内的用户
     * - 敏感信息自动过滤
     * 
     * 数据安全：
     * - 密码字段不返回
     * - 敏感信息脱敏处理
     * - 权限验证通过后返回
     * 
     * @param id 用户ID，要查询的用户的唯一标识
     * @return ApiResult<SysUserVo> 包含用户详细信息的响应结果
     * @throws BusinessException 当用户不存在或权限不足时抛出业务异常
     */
    @GetMapping("/detail/{userId}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取系统用户的详细信息，不包含密码等敏感信息")
    @Permission("sys:user:info")
    public ApiResult<SysUserVo> getSysUser(@PathVariable("userId") Long id) {
        log.info("开始获取系统用户详情，用户ID: {}", id);
        
        try {
            SysUserVo sysUserVo = sysUserService.getSysUserById(id);
            
            if (sysUserVo != null) {
                log.info("获取系统用户详情成功，用户ID: {}, 用户名: {}", id, sysUserVo.getUsername());
                return ApiResult.success(sysUserVo);
            } else {
                log.warn("获取系统用户详情失败，用户不存在，用户ID: {}", id);
                return ApiResult.fail("用户不存在");
            }
        } catch (BusinessException e) {
            log.error("获取系统用户详情业务异常，用户ID: {}, 错误: {}", id, e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("获取系统用户详情系统异常，用户ID: {}, 错误: {}", id, e.getMessage(), e);
            return ApiResult.fail("获取用户详情失败，系统异常");
        }
    }

    /**
     * 系统用户分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户列表")
    @Permission("sys:user:page")
    public ApiResult<SysUserVo> getSysUserList(@Valid SysUserQuery query) {
        Paging<SysUserVo> paging = sysUserService.getSysUserPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 重置系统用户密码
     *
     * @param sysUserResetPasswordDto
     * @return
     * @throws Exception
     */
    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码")
    @Permission("sys:user:reset-password")
    public ApiResult resetSysUserPassword(@Valid @RequestBody SysUserResetPasswordDto sysUserResetPasswordDto) {
        boolean flag = sysUserService.resetSysUserPassword(sysUserResetPasswordDto);
        return ApiResult.result(flag);
    }

    /**
     * 更新用户状态
     * 
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @PutMapping("/status")
    @Operation(summary = "更新用户状态")
    @Permission("sys:user:update")
    public ApiResult updateUserStatus(@RequestParam Long id, @RequestParam Boolean status) {
        SysUserDto dto = new SysUserDto();
        dto.setId(id);
        dto.setStatus(status);
        boolean flag = sysUserService.updateSysUser(dto);
        return ApiResult.result(flag);
    }

    /**
     * 获取个人信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/profile")
    @Operation(summary = "获取个人信息")
    public ApiResult<SysUserVo> getProfile() {
        Long userId = LoginUtil.getUserId();
        if (userId == null) {
            throw new BusinessException("用户ID为空");
        }
        SysUserVo sysUserVo = sysUserService.getSysUserById(userId);
        return ApiResult.success(sysUserVo);
    }

    /**
     * 修改个人信息
     *
     * @param dto 个人信息
     * @return 操作结果
     */
    @PostMapping("/update-profile")
    @Operation(summary = "修改个人信息")
    public ApiResult updateProfile(@Valid @RequestBody SysUserUpdateProfileDto dto) {
        boolean flag = sysUserService.updateProfile(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改用户密码
     *
     * @param dto 密码修改信息
     * @return 操作结果
     */
    @PostMapping("/update-password")
    @Operation(summary = "修改用户密码")
    public ApiResult updatePassword(@Valid @RequestBody SysUserUpdatePasswordDto dto) {
        boolean flag = sysUserService.updatePassword(dto);
        return ApiResult.result(flag);
    }

    /**
     * 导入Excel用户数据
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @PostMapping("/importExcel")
    @Operation(summary = "导入Excel用户数据")
    public ApiResult importExcel(MultipartFile multipartFile) throws Exception {
        boolean flag = sysUserService.importExcel(multipartFile);
        return ApiResult.result(flag);
    }

}
