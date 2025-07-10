package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppNotificationQuery;
import com.fourth.medical.medical.service.NotificationService;
import com.fourth.medical.medical.vo.AppNotificationVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App系统通知 控制器
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App系统通知")
@RequestMapping("/app/notification")
public class AppNotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取App系统通知详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App系统通知详情")
    @PostMapping("/getAppNotification/{id}")
    public ApiResult<AppNotificationVo> getAppNotification(@PathVariable Long id) {
        log.info("获取App系统通知详情：{}", id);
        AppNotificationVo appNotificationVo = notificationService.getAppNotificationById(id);
        return ApiResult.success(appNotificationVo);
    }

    /**
     * 获取App系统通知分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App系统通知分页列表")
    @PostMapping("/getAppNotificationPage")
    public ApiResult<AppNotificationVo> getAppNotificationPage(@Valid @RequestBody AppNotificationQuery query) {
        log.info("获取App系统通知分页列表：{}", query);
        Paging<AppNotificationVo> paging = notificationService.getAppNotificationPage(query);
        return ApiResult.success(paging);
    }

}
