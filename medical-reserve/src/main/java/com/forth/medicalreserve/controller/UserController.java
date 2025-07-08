package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.entity.Users;
import com.forth.medicalreserve.service.UserService;
import com.forth.medicalreserve.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Users> register(@RequestBody Users user) {
        try {
            Users registeredUser = userService.register(user);
            return Result.success(registeredUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Users> login(@RequestParam String userName, @RequestParam String password) {
        try {
            Users user = userService.login(userName, password);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("手机号登录")
    @PostMapping("/loginByMobile")
    public Result<Users> loginByMobile(@RequestParam String mobile, @RequestParam String code) {
        try {
            Users user = userService.loginByMobile(mobile, code);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取用户信息")
    @GetMapping("/{userId}")
    public Result<Users> getUserInfo(@PathVariable Integer userId) {
        try {
            Users user = userService.findById(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新用户信息")
    @PutMapping
    public Result<Users> updateUser(@RequestBody Users user) {
        try {
            Users updatedUser = userService.updateUser(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("修改密码")
    @PutMapping("/{userId}/password")
    public Result<Boolean> changePassword(
            @PathVariable Integer userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            boolean result = userService.changePassword(userId, oldPassword, newPassword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result<Boolean> resetPassword(
            @RequestParam String mobile,
            @RequestParam String code,
            @RequestParam String newPassword) {
        try {
            boolean result = userService.resetPassword(mobile, code, newPassword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 