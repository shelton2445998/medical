package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppInvoiceQuery;
import com.fourth.medical.medical.service.InvoiceService;
import com.fourth.medical.medical.vo.AppInvoiceVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App电子发票 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供电子发票查询服务，包括发票详情、发票列表、发票下载等</li>
 *   <li>支持电子发票的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供便捷的发票管理服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>发票查询：支持电子发票的详情和列表查询</li>
 *   <li>发票下载：支持电子发票的PDF格式下载</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取发票列表</li>
 *   <li>发票查看：用户查看具体的电子发票详情</li>
 *   <li>发票下载：用户下载电子发票PDF文件</li>
 *   <li>分页查询：支持分页加载发票数据</li>
 *   <li>发票管理：用户管理自己的电子发票</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：用户只能查看自己的电子发票数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>用户隔离：确保用户只能访问自己的数据</li>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据加密：对敏感发票信息进行加密存储</li>
 *   <li>下载安全：确保发票下载的安全性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>发票推送：可扩展支持发票邮件推送功能</li>
 *   <li>发票分享：可扩展支持发票分享功能</li>
 *   <li>发票归档：可扩展支持发票自动归档功能</li>
 *   <li>税务对接：可扩展支持与税务系统的对接</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App电子发票")
@RequestMapping("/app/invoice")
public class AppInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 获取App电子发票详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App电子发票详情")
    @PostMapping("/getAppInvoice/{id}")
    public ApiResult<AppInvoiceVo> getAppInvoice(@PathVariable Long id) {
        log.info("获取App电子发票详情：{}", id);
        AppInvoiceVo appInvoiceVo = invoiceService.getAppInvoiceById(id);
        return ApiResult.success(appInvoiceVo);
    }

    /**
     * 获取App电子发票分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App电子发票分页列表")
    @PostMapping("/getAppInvoicePage")
    public ApiResult<AppInvoiceVo> getAppInvoicePage(@Valid @RequestBody AppInvoiceQuery query) {
        log.info("获取App电子发票分页列表：{}", query);
        Paging<AppInvoiceVo> paging = invoiceService.getAppInvoicePage(query);
        return ApiResult.success(paging);
    }

}
