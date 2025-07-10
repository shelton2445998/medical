package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Invoice;
import com.fourth.medical.medical.query.InvoiceQuery;
import com.fourth.medical.medical.vo.InvoiceVo;
import com.fourth.medical.medical.query.AppInvoiceQuery;
import com.fourth.medical.medical.vo.AppInvoiceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 电子发票 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

    /**
     * 电子发票详情
     *
     * @param id
     * @return
     */
    InvoiceVo getInvoiceById(Long id);

    /**
     * 电子发票分页列表
     *
     * @param query
     * @return
     */
    List<InvoiceVo> getInvoicePage(InvoiceQuery query);

    /**
     * App电子发票详情
     *
     * @param id
     * @return
     */
    AppInvoiceVo getAppInvoiceById(Long id);

    /**
     * App电子发票分页列表
     *
     * @param query
     * @return
     */
    List<AppInvoiceVo> getAppInvoicePage(AppInvoiceQuery query);

}
