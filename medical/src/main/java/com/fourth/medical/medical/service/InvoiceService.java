package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.InvoiceDto;
import com.fourth.medical.medical.entity.Invoice;
import com.fourth.medical.medical.query.InvoiceQuery;
import com.fourth.medical.medical.vo.InvoiceVo;
import com.fourth.medical.medical.query.AppInvoiceQuery;
import com.fourth.medical.medical.vo.AppInvoiceVo;


/**
 * 电子发票 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface InvoiceService extends IService<Invoice> {

    /**
     * 添加电子发票
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addInvoice(InvoiceDto dto);

    /**
     * 修改电子发票
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateInvoice(InvoiceDto dto);

    /**
     * 删除电子发票
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInvoice(Long id);

    /**
     * 电子发票详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    InvoiceVo getInvoiceById(Long id);

    /**
     * 电子发票分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<InvoiceVo> getInvoicePage(InvoiceQuery query);

    /**
     * App电子发票详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppInvoiceVo getAppInvoiceById(Long id);

    /**
     * App电子发票分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppInvoiceVo> getAppInvoicePage(AppInvoiceQuery query);

}
