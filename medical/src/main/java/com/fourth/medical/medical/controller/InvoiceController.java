package com.fourth.medical.medical.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.dto.InvoiceDto;
import com.fourth.medical.medical.query.InvoiceQuery;
import com.fourth.medical.medical.service.InvoiceService;
import com.fourth.medical.medical.vo.InvoiceVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 电子发票 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>管理体检服务的电子发票信息，包括发票生成、查询、下载等功能</li>
 *   <li>支持电子发票的增删改查操作，为用户提供发票管理服务</li>
 *   <li>提供电子发票的分页查询和详情查看功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>发票管理：支持电子发票的创建、编辑、删除和查询</li>
 *   <li>发票生成：支持根据体检订单自动生成电子发票</li>
 *   <li>发票下载：支持电子发票的PDF格式下载</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>操作日志：记录所有关键操作，便于审计和追踪</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>发票生成：根据体检订单信息自动生成电子发票</li>
 *   <li>发票查询：支持按条件查询和分页展示发票列表</li>
 *   <li>发票下载：用户下载电子发票PDF文件</li>
 *   <li>发票维护：根据需要进行发票信息的修改或删除</li>
 *   <li>发票统计：提供发票相关的统计报表功能</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>invoice:add - 添加发票权限</li>
 *   <li>invoice:update - 修改发票权限</li>
 *   <li>invoice:delete - 删除发票权限</li>
 *   <li>invoice:info - 查看发票详情权限</li>
 *   <li>invoice:page - 查看发票列表权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>输入验证：使用@Valid注解进行参数验证</li>
 *   <li>SQL注入防护：使用MyBatis参数化查询</li>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>操作日志：记录所有关键操作，便于安全审计</li>
 *   <li>数据加密：对敏感发票信息进行加密存储</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>发票模板：可扩展支持自定义发票模板</li>
 *   <li>批量生成：可扩展支持批量生成电子发票</li>
 *   <li>发票推送：可扩展支持发票邮件推送功能</li>
 *   <li>税务对接：可扩展支持与税务系统的对接</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "电子发票")
@RequestMapping("/admin/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 添加电子发票
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.ADD)
    @Operation(summary = "添加电子发票")
    @PostMapping("/addInvoice")
    @Permission("invoice:add")
    public ApiResult addInvoice(@Valid @RequestBody InvoiceDto dto) {
        log.info("添加电子发票：{}", dto);
        boolean flag = invoiceService.addInvoice(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改电子发票
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.UPDATE)
    @Operation(summary = "修改电子发票")
    @PostMapping("/updateInvoice")
    @Permission("invoice:update")
    public ApiResult updateInvoice(@Valid @RequestBody InvoiceDto dto) {
        log.info("修改电子发票：{}", dto);
        boolean flag = invoiceService.updateInvoice(dto);
        return ApiResult.result(flag);
    }

    /**
     * 删除电子发票
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(type = SysLogType.DELETE)
    @Operation(summary = "删除电子发票")
    @PostMapping("/deleteInvoice/{id}")
    @Permission("invoice:delete")
    public ApiResult deleteInvoice(@PathVariable Long id) {
        log.info("删除电子发票：{}", id);
        boolean flag = invoiceService.deleteInvoice(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取电子发票详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取电子发票详情")
    @PostMapping("/getInvoice/{id}")
    @Permission("invoice:info")
    public ApiResult<InvoiceVo> getInvoice(@PathVariable Long id) {
        log.info("获取电子发票详情：{}", id);
        InvoiceVo invoiceVo = invoiceService.getInvoiceById(id);
        return ApiResult.success(invoiceVo);
    }

    /**
     * 获取电子发票分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取电子发票分页列表")
    @PostMapping("/getInvoicePage")
    @Permission("invoice:page")
    public ApiResult<InvoiceVo> getInvoicePage(@Valid @RequestBody InvoiceQuery query) {
        log.info("获取电子发票分页列表：{}", query);
        Paging<InvoiceVo> paging = invoiceService.getInvoicePage(query);
        return ApiResult.success(paging);
    }

}
