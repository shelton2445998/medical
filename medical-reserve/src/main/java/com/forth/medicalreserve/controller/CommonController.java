package com.forth.medicalreserve.controller;

import com.forth.medicalreserve.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共接口控制器
 */
@Api(tags = "公共接口")
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name:医疗预约系统}")
    private String applicationName;

    /**
     * 系统信息
     */
    @ApiOperation("获取系统信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", applicationName);
        info.put("version", "1.0.0");
        info.put("port", serverPort);
        info.put("startTime", System.currentTimeMillis());
        return Result.success(info);
    }

    /**
     * 健康检查
     */
    @ApiOperation("健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("OK");
    }
} 