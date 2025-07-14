package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.mapper.ReportMapper;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.ReportVo;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.vo.AppReportVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 体检报告总 服务实现类
 *
 * @author fourth
 * @since 2025-07-14
 */
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addReport(ReportDto dto) {
        Report report = new Report();
        BeanUtils.copyProperties(dto, report);
        return save(report);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateReport(ReportDto dto) {
        Long id = dto.getId();
        Report report = getById(id);
        if (report == null) {
            throw new BusinessException("体检报告总不存在");
        }
        BeanUtils.copyProperties(dto, report);
        return updateById(report);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteReport(Long id) {
        return removeById(id);
    }

    @Override
    public ReportVo getReportById(Long id) {
        return reportMapper.getReportById(id);
    }

    @Override
    public Paging<ReportVo> getReportPage(ReportQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<ReportVo> list = reportMapper.getReportPage(query);
        Paging<ReportVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppReportVo getAppReportById(Long id) {
        return reportMapper.getAppReportById(id);
    }

    @Override
    public Paging<AppReportVo> getAppReportPage(AppReportQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppReportVo> list = reportMapper.getAppReportPage(query);
        Paging<AppReportVo> paging = new Paging<>(list);
        return paging;
    }

}
