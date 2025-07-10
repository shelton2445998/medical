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
