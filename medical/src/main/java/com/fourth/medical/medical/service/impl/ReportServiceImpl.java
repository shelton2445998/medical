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
        
        return baseMapper.getAppReportPage(PageUtil.buildPage(query), query);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReportForOrder(Long orderId, Long userId, String checkitemIds, Long doctorId) {
        log.info("创建体检报告，订单ID：{}，用户ID：{}，检查项：{}，医生ID：{}", orderId, userId, checkitemIds, doctorId);
        
        try {
            // 先检查是否已经存在该订单的报告
            List<Report> existingReports = baseMapper.selectList(
                    new LambdaQueryWrapper<Report>()
                            .eq(Report::getOrderId, orderId));
            
            if (existingReports != null && !existingReports.isEmpty()) {
                // 如果存在多个报告，取第一个
                Report existingReport = existingReports.get(0);
                log.info("订单已存在报告，报告ID：{}", existingReport.getId());
                return existingReport.getId();
            }
            
            // 创建新的报告
            Report report = new Report();
            report.setOrderId(orderId);
            report.setUserId(userId);
            report.setCheckitemIds(checkitemIds);
            report.setDoctorId(doctorId);
            report.setStatus(0);  // 未完成状态
            
            // 插入数据库
            baseMapper.insert(report);
            log.info("成功创建体检报告，报告ID：{}", report.getId());
            
            return report.getId();
        } catch (Exception e) {
            log.error("创建体检报告失败", e);
            throw new BusinessException("创建体检报告失败");
        }
    }
}
