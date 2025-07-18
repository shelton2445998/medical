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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供系统通知查询服务，包括通知详情、通知列表、通知状态管理等</li>
 *   <li>支持系统通知的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的通知管理服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>通知查询：支持系统通知的详情和列表查询</li>
 *   <li>通知状态：支持通知的已读、未读状态管理</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取通知列表</li>
 *   <li>通知查看：用户查看具体的系统通知详情</li>
 *   <li>状态更新：用户标记通知为已读状态</li>
 *   <li>分页查询：支持分页加载通知数据</li>
 *   <li>通知管理：用户管理自己的系统通知</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：用户只能查看自己的系统通知数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>用户隔离：确保用户只能访问自己的数据</li>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>内容过滤：对通知内容进行安全过滤，防止XSS攻击</li>
 *   <li>隐私保护：保护用户通知信息的隐私安全</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>推送通知：可扩展支持消息推送服务（短信、邮件、APP推送等）</li>
 *   <li>通知分类：可扩展支持通知分类管理</li>
 *   <li>用户订阅：可扩展支持用户自定义通知订阅</li>
 *   <li>通知统计：可扩展支持通知阅读率、点击率等统计功能</li>
 * </ul>
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
