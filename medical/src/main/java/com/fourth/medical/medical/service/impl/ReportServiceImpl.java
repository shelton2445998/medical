package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.mapper.ReportMapper;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.medical.vo.AppReportVo;
import com.fourth.medical.medical.vo.ReportVo;
import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;

/**
 * 体检报告总 服务实现类
 *
 * @author fourth
 * @since 2025-07-14
 */
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Override
    public boolean addReport(ReportDto dto) {
        log.info("添加体检报告总：{}", dto);
        Report report = new Report();
        BeanUtils.copyProperties(dto, report);
        return save(report);
    }

    @Override
    public boolean updateReport(ReportDto dto) {
        log.info("修改体检报告总：{}", dto);
        Report report = getById(dto.getId());
        if (report == null) {
            throw new BusinessException("体检报告总不存在");
        }
        BeanUtils.copyProperties(dto, report);
        return updateById(report);
    }

    @Override
    public boolean deleteReport(Long id) {
        log.info("删除体检报告总：{}", id);
        if (id == null) {
            throw new BusinessException("ID不能为空");
        }
        return removeById(id);
    }

    @Override
    public ReportVo getReportById(Long id) {
        log.info("获取体检报告总详情：{}", id);
        if (id == null) {
            throw new BusinessException("ID不能为空");
        }
        return baseMapper.getReportById(id);
    }

    @Override
    public Paging<ReportVo> getReportPage(ReportQuery query) {
        log.info("获取体检报告总分页列表：{}", query);
        return baseMapper.getReportPage(PageUtil.buildPage(query), query);
    }

    @Override
    public AppReportVo getAppReportById(Long id, String token) {
        log.info("获取App体检报告总详情：{}", id);
        if (id == null) {
            throw new BusinessException("ID不能为空");
        }
        
        // 正确的方式：先用token获取到LoginVo，再获取用户信息
        AppLoginVo appLoginVo = AppLoginUtil.getLoginVo(token);
        if (appLoginVo == null) {
            throw new BusinessException("无效的用户Token");
        }
        Long userId = appLoginVo.getUserId();
        
        // 查询报告
        AppReportVo appReportVo = baseMapper.getAppReportById(id);
        if (appReportVo == null) {
            throw new BusinessException("体检报告不存在");
        }
        
        // 检查报告是否属于该用户
        if (!userId.equals(appReportVo.getUserId())) {
            throw new BusinessException("无权限查看该体检报告");
        }
        
        return appReportVo;
    }

    @Override
    public Paging<AppReportVo> getAppReportPage(AppReportQuery query, String token) {
        log.info("获取App体检报告总分页列表：{}", query);
        
        // 正确的方式：先用token获取到LoginVo，再获取用户信息
        AppLoginVo appLoginVo = AppLoginUtil.getLoginVo(token);
        if (appLoginVo == null) {
            throw new BusinessException("无效的用户Token");
        }
        Long userId = appLoginVo.getUserId();
        
        // 设置查询条件，只查询当前用户的报告
        query.setUserId(userId);
        
        // 用分页对象调用mapper
        IPage<AppReportVo> page = baseMapper.getAppReportPage(PageUtil.buildPage(query), query);
        // 手动组装 Paging 返回
        Paging<AppReportVo> paging = new Paging<>();
        paging.setList(page.getRecords());
        paging.setTotal(page.getTotal());
        paging.setPageIndex(query.getPageIndex());
        paging.setPageSize(query.getPageSize());
        return paging;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReportForOrder(Long orderId, Long userId, String checkitemIds) {
        log.info("为订单创建体检报告：orderId={}, userId={}, checkitemIds={}", orderId, userId, checkitemIds);
        
        // 创建报告对象
        Report report = new Report();
        report.setOrderId(orderId);
        report.setUserId(userId);
        report.setCheckitemIds(checkitemIds);
        report.setStatus(0); // 初始状态：未完成
        report.setCreateTime(new Date());
        
        // 保存报告
        save(report);
        
        log.info("体检报告创建成功，报告ID：{}", report.getId());
        return report.getId();
    }

    @Override
    public boolean deleteAppReport(Long id, String token) {
        log.info("删除App体检报告：id={}, token={}", id, token);
        
        if (id == null) {
            throw new BusinessException("报告ID不能为空");
        }
        
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("用户未登录");
        }
        
        // 获取用户信息 - 使用AppLoginUtil而不是LoginUtil
        AppLoginVo appLoginVo = AppLoginUtil.getLoginVo(token);
        if (appLoginVo == null) {
            throw new BusinessException("无法获取用户信息，请重新登录");
        }
        
        Long userId = appLoginVo.getUserId();
        
        // 查询报告信息
        Report report = getById(id);
        if (report == null) {
            throw new BusinessException("报告不存在");
        }
        
        // 验证报告是否属于当前用户
        if (!userId.equals(report.getUserId())) {
            throw new BusinessException("只能删除自己的报告");
        }
        
        // 检查报告状态，已完成的报告不能删除
        if (report.getStatus() != null && report.getStatus() == 1) {
            throw new BusinessException("已完成的报告不能删除");
        }
        
        // 执行删除操作（物理删除，因为Report表没有逻辑删除字段）
        boolean result = removeById(id);
        
        if (result) {
            log.info("App体检报告删除成功：id={}, userId={}", id, userId);
        } else {
            log.error("App体检报告删除失败：id={}, userId={}", id, userId);
        }
        
        return result;
    }
}
