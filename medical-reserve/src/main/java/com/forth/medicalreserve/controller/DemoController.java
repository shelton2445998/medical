package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示控制器
 */
@Api(tags = "演示接口")
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    /**
     * 公共接口，不需要认证
     */
    @ApiOperation("公共接口")
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        return Result.success("这是一个公共接口，不需要认证");
    }

    /**
     * 用户接口，需要USER角色
     */
    @ApiOperation("用户接口")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public Result<String> userEndpoint() {
        return Result.success("这是一个用户接口，需要USER角色");
    }

    /**
     * 管理员接口，需要ADMIN角色
     */
    @ApiOperation("管理员接口")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Result<String> adminEndpoint() {
        return Result.success("这是一个管理员接口，需要ADMIN角色");
    }

    /**
     * 医生接口，需要DOCTOR角色
     */
    @ApiOperation("医生接口")
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public Result<String> doctorEndpoint() {
        return Result.success("这是一个医生接口，需要DOCTOR角色");
    }
} 