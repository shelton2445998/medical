package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.InvoiceDto;
import com.fourth.medical.medical.entity.Invoice;
import com.fourth.medical.medical.mapper.InvoiceMapper;
import com.fourth.medical.medical.query.InvoiceQuery;
import com.fourth.medical.medical.service.InvoiceService;
import com.fourth.medical.medical.vo.InvoiceVo;
import com.fourth.medical.medical.query.AppInvoiceQuery;
import com.fourth.medical.medical.vo.AppInvoiceVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 电子发票 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addInvoice(InvoiceDto dto) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(dto, invoice);
        return save(invoice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInvoice(InvoiceDto dto) {
        Long id = dto.getId();
        Invoice invoice = getById(id);
        if (invoice == null) {
            throw new BusinessException("电子发票不存在");
        }
        BeanUtils.copyProperties(dto, invoice);
        return updateById(invoice);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInvoice(Long id) {
        return removeById(id);
    }

    @Override
    public InvoiceVo getInvoiceById(Long id) {
        return invoiceMapper.getInvoiceById(id);
    }

    @Override
    public Paging<InvoiceVo> getInvoicePage(InvoiceQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<InvoiceVo> list = invoiceMapper.getInvoicePage(query);
        Paging<InvoiceVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppInvoiceVo getAppInvoiceById(Long id) {
        return invoiceMapper.getAppInvoiceById(id);
    }

    @Override
    public Paging<AppInvoiceVo> getAppInvoicePage(AppInvoiceQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppInvoiceVo> list = invoiceMapper.getAppInvoicePage(query);
        Paging<AppInvoiceVo> paging = new Paging<>(list);
        return paging;
    }

}
