package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.common.Result;
import com.forth.medicalreserve.dto.UpdatePasswordDTO;
import com.forth.medicalreserve.dto.UserDTO;
import com.forth.medicalreserve.entity.User;
import com.forth.medicalreserve.exception.BusinessException;
import com.forth.medicalreserve.security.JwtUserDetails;
import com.forth.medicalreserve.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public Result<User> getCurrentUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        User user = userService.getById(userDetails.getId());
        // 清除敏感信息
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 修改当前用户密码
     */
    @ApiOperation("修改当前用户密码")
    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> updatePassword(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        
        // 验证新密码和确认密码是否一致
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
            throw new BusinessException("新密码和确认密码不一致");
        }
        
        userService.updatePassword(
                userDetails.getId(),
                updatePasswordDTO.getOldPassword(),
                updatePasswordDTO.getNewPassword()
        );
        return Result.success();
    }

    /**
     * 修改当前用户信息
     */
    @ApiOperation("修改当前用户信息")
    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> updateCurrentUser(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @Valid @RequestBody UserDTO userDTO) {
        
        userService.update(userDetails.getId(), userDTO);
        return Result.success();
    }

    /**
     * 分页查询用户列表（管理员权限）
     */
    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<User>> page(
            @ApiParam(value = "页码", required = true) @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", required = true) @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "关键词") @RequestParam(required = false) String keyword) {
        
        Page<User> page = userService.page(pageNum, pageSize, keyword);
        // 清除敏感信息
        page.getContent().forEach(user -> user.setPassword(null));
        return Result.success(page);
    }

    /**
     * 获取用户详情（管理员权限）
     */
    @ApiOperation("获取用户详情")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<User> getUser(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        // 清除敏感信息
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 创建用户（管理员权限）
     */
    @ApiOperation("创建用户")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Integer> createUser(@Valid @RequestBody UserDTO userDTO) {
        Integer userId = userService.create(userDTO);
        return Result.success(userId);
    }

    /**
     * 更新用户（管理员权限）
     */
    @ApiOperation("更新用户")
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUser(
            @PathVariable Integer userId,
            @Valid @RequestBody UserDTO userDTO) {
        
        userService.update(userId, userDTO);
        return Result.success();
    }

    /**
     * 删除用户（管理员权限）
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Integer userId) {
        userService.delete(userId);
        return Result.success();
    }

    /**
     * 禁用/启用用户（管理员权限）
     */
    @ApiOperation("禁用/启用用户")
    @PutMapping("/{userId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUserStatus(
            @PathVariable Integer userId,
            @PathVariable Integer status) {
        
        userService.updateStatus(userId, status);
        return Result.success();
    }
} 