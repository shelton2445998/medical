package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDetailDto;
import com.fourth.medical.medical.entity.ReportItemDetail;
import com.fourth.medical.medical.mapper.ReportItemDetailMapper;
import com.fourth.medical.medical.query.ReportItemDetailQuery;
import com.fourth.medical.medical.service.ReportItemDetailService;
import com.fourth.medical.medical.vo.ReportItemDetailVo;
import com.fourth.medical.medical.query.AppReportItemDetailQuery;
import com.fourth.medical.medical.vo.AppReportItemDetailVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 体检报告检查项明细 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class ReportItemDetailServiceImpl extends ServiceImpl<ReportItemDetailMapper, ReportItemDetail> implements ReportItemDetailService {

    @Autowired
    private ReportItemDetailMapper reportItemDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addReportItemDetail(ReportItemDetailDto dto) {
        ReportItemDetail reportItemDetail = new ReportItemDetail();
        BeanUtils.copyProperties(dto, reportItemDetail);
        return save(reportItemDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateReportItemDetail(ReportItemDetailDto dto) {
        Long id = dto.getId();
        ReportItemDetail reportItemDetail = getById(id);
        if (reportItemDetail == null) {
            throw new BusinessException("体检报告检查项明细不存在");
        }
        BeanUtils.copyProperties(dto, reportItemDetail);
        return updateById(reportItemDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteReportItemDetail(Long id) {
        return removeById(id);
    }

    @Override
    public ReportItemDetailVo getReportItemDetailById(Long id) {
        return reportItemDetailMapper.getReportItemDetailById(id);
    }

    @Override
    public Paging<ReportItemDetailVo> getReportItemDetailPage(ReportItemDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<ReportItemDetailVo> list = reportItemDetailMapper.getReportItemDetailPage(query);
        Paging<ReportItemDetailVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppReportItemDetailVo getAppReportItemDetailById(Long id) {
        return reportItemDetailMapper.getAppReportItemDetailById(id);
    }

    @Override
    public Paging<AppReportItemDetailVo> getAppReportItemDetailPage(AppReportItemDetailQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppReportItemDetailVo> list = reportItemDetailMapper.getAppReportItemDetailPage(query);
        Paging<AppReportItemDetailVo> paging = new Paging<>(list);
        return paging;
    }

}
