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
