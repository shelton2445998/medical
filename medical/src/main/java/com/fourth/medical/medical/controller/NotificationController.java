package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.NotificationDto;
import com.fourth.medical.medical.query.NotificationQuery;
import com.fourth.medical.medical.service.NotificationService;
import com.fourth.medical.medical.vo.NotificationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统通知 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "系统通知")
@RequestMapping("/admin/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 添加系统通知
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加系统通知")
    @PostMapping("/addNotification")
    @Permission("notification:add")
    public ApiResult addNotification(@Valid @RequestBody NotificationDto dto) {
        log.info("添加系统通知：{}", dto);
        boolean flag = notificationService.addNotification(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改系统通知
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改系统通知")
    @PostMapping("/updateNotification")
    @Permission("notification:update")
    public ApiResult updateNotification(@Valid @RequestBody NotificationDto dto) {
        log.info("修改系统通知：{}", dto);
        boolean flag = notificationService.updateNotification(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除系统通知
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除系统通知")
    @PostMapping("/deleteNotification/{id}")
    @Permission("notification:delete")
    public ApiResult deleteNotification(@PathVariable Long id) {
        log.info("删除系统通知：{}", id);
        boolean flag = notificationService.deleteNotification(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取系统通知详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取系统通知详情")
    @PostMapping("/getNotification/{id}")
    @Permission("notification:info")
    public ApiResult<NotificationVo> getNotification(@PathVariable Long id) {
        log.info("获取系统通知详情：{}", id);
        NotificationVo notificationVo = notificationService.getNotificationById(id);
        return ApiResult.success(notificationVo);
    }

    /**
     * 获取系统通知分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取系统通知分页列表")
    @PostMapping("/getNotificationPage")
    @Permission("notification:page")
    public ApiResult<NotificationVo> getNotificationPage(@Valid @RequestBody NotificationQuery query) {
        log.info("获取系统通知分页列表：{}", query);
        Paging<NotificationVo> paging = notificationService.getNotificationPage(query);
        return ApiResult.success(paging);
    }

}
