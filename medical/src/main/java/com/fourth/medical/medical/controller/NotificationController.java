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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理系统内部的通知消息，包括系统公告、业务提醒、用户通知等</li>
 *   <li>支持通知的增删改查操作，为系统提供统一的消息通知服务</li>
 *   <li>提供通知的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>通知管理：支持系统通知的创建、编辑、删除和查询</li>
 *   <li>通知分类：支持不同类型的通知分类管理</li>
 *   <li>通知状态：支持通知的发布、撤回、过期等状态管理</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>通知创建：管理员创建系统通知，设置通知内容、类型、有效期等</li>
 *   <li>通知发布：将创建的通知发布给目标用户或用户组</li>
 *   <li>通知查询：支持按条件查询和分页展示通知列表</li>
 *   <li>通知维护：根据需要进行通知的修改、撤回或删除</li>
 *   <li>通知过期：自动处理过期通知的状态更新</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>notification:add - 添加通知权限</li>
 *   <li>notification:update - 修改通知权限</li>
 *   <li>notification:delete - 删除通知权限</li>
 *   <li>notification:info - 查看通知详情权限</li>
 *   <li>notification:page - 查看通知列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>内容过滤：对通知内容进行安全过滤，防止XSS攻击</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>推送服务：可扩展支持消息推送服务（短信、邮件、APP推送等）</li>
 *   <li>通知模板：可扩展支持通知模板功能</li>
 *   <li>用户订阅：可扩展支持用户自定义通知订阅</li>
 *   <li>通知统计：可扩展支持通知阅读率、点击率等统计功能</li>
 * </ul>
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
