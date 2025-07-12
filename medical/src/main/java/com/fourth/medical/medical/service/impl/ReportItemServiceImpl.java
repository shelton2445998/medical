package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.mapper.ReportItemMapper;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 体检报告检查项信息 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class ReportItemServiceImpl extends ServiceImpl<ReportItemMapper, ReportItem> implements ReportItemService {

    @Autowired
    private ReportItemMapper reportItemMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addReportItem(ReportItemDto dto) {
        ReportItem reportItem = new ReportItem();
        BeanUtils.copyProperties(dto, reportItem);
        return save(reportItem);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateReportItem(ReportItemDto dto) {
        Long id = dto.getId();
        ReportItem reportItem = getById(id);
        if (reportItem == null) {
            throw new BusinessException("体检报告检查项信息不存在");
        }
        BeanUtils.copyProperties(dto, reportItem);
        return updateById(reportItem);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteReportItem(Long id) {
        return removeById(id);
    }

    @Override
    public ReportItemVo getReportItemById(Long id) {
        return reportItemMapper.getReportItemById(id);
    }

    @Override
    public Paging<ReportItemVo> getReportItemPage(ReportItemQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<ReportItemVo> list = reportItemMapper.getReportItemPage(query);
        Paging<ReportItemVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppReportItemVo getAppReportItemById(Long id) {
        return reportItemMapper.getAppReportItemById(id);
    }

    @Override
    public Paging<AppReportItemVo> getAppReportItemPage(AppReportItemQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppReportItemVo> list = reportItemMapper.getAppReportItemPage(query);
        Paging<AppReportItemVo> paging = new Paging<>(list);
        return paging;
    }
    
    @Override
    public List<ReportItemVo> getReportItemsByUserId(Long userId) {
        return reportItemMapper.getReportItemsByUserId(userId);
    }
}
