package com.fourth.medical.system.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysUserDto;
import com.fourth.medical.system.dto.SysUserResetPasswordDto;
import com.fourth.medical.system.dto.SysUserUpdatePasswordDto;
import com.fourth.medical.system.dto.SysUserUpdateProfileDto;
import com.fourth.medical.system.query.SysUserQuery;
import com.fourth.medical.system.service.SysUserService;
import com.fourth.medical.system.vo.SysUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 系统用户 控制器
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Slf4j
@RestController
@Tag(name = "系统用户")
@RequestMapping("/admin/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 添加系统用户
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    @Operation(summary = "添加用户")
    @Permission("sys:user:add")
    public ApiResult addSysUser(@Valid @RequestBody SysUserDto dto) {
        boolean flag = sysUserService.addSysUser(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统用户
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户")
    @Permission("sys:user:update")
    public ApiResult updateSysUser(@Valid @RequestBody SysUserDto dto) {
        boolean flag = sysUserService.updateSysUser(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户")
    @Permission("sys:user:delete")
    public ApiResult deleteSysUser(@PathVariable Long id) {
        boolean flag = sysUserService.deleteSysUser(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统用户详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/detail/{userId}")
    @Operation(summary = "获取用户详情")
    @Permission("sys:user:info")
    public ApiResult<SysUserVo> getSysUser(@PathVariable("userId") Long id) {
        SysUserVo sysUserVo = sysUserService.getSysUserById(id);
        return ApiResult.success(sysUserVo);
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
     * @param sysUserUpdateProfileDto
     * @return
     * @throws Exception
     */
    @PutMapping("/profile")
    @Operation(summary = "修改个人信息")
    public ApiResult updateProfile(@Valid @RequestBody SysUserUpdateProfileDto sysUserUpdateProfileDto) {
        boolean flag = sysUserService.updateProfile(sysUserUpdateProfileDto);
        return ApiResult.result(flag);
    }

    /**
     * 修改用户密码
     *
     * @param sysUserUpdatePasswordDto
     * @return
     * @throws Exception
     */
    @PutMapping("/password")
    @Operation(summary = "修改用户密码")
    public ApiResult updatePassword(@Valid @RequestBody SysUserUpdatePasswordDto sysUserUpdatePasswordDto) {
        boolean flag = sysUserService.updatePassword(sysUserUpdatePasswordDto);
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
