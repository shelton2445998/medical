package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.mapper.ReportItemMapper;
import com.fourth.medical.medical.service.DoctorReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生报告服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class DoctorReportServiceImpl implements DoctorReportService {

    @Autowired
    private ReportItemMapper reportItemMapper;

    /**
     * 统计医生待处理的报告数量
     * 待处理报告定义为conclusion字段为空的报告
     *
     * @param doctorId 医生ID
     * @return 待处理报告数量
     */
    @Override
    public Integer countPendingReportsByDoctorId(Long doctorId) {
        log.info("统计医生[{}]待处理报告数量", doctorId);
        
        try {
            // 查询条件：待处理报告定义为conclusion为空的报告
            LambdaQueryWrapper<ReportItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReportItem::getDoctorId, doctorId)
                   .and(w -> w.isNull(ReportItem::getConclusion)
                           .or()
                           .eq(ReportItem::getConclusion, ""));
            
            // 统计符合条件的记录数
            Long count = reportItemMapper.selectCount(wrapper);
            log.info("医生[{}]待处理报告数量：{}", doctorId, count);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.error("统计医生待处理报告数量出错", e);
            return 0;
        }
    }

    /**
     * 统计医生本月的体检报告数量
     * 统计该医生名下的所有报告数量，无论状态如何
     *
     * @param doctorId 医生ID
     * @return 本月体检报告数量
     */
    @Override
    public Integer countMonthReportsByDoctorId(Long doctorId) {
        log.info("统计医生[{}]本月体检报告数量", doctorId);
        
        try {
            // 获取当前月份的起始日期和结束日期
            LocalDate now = LocalDate.now();
            LocalDate firstDayOfMonth = now.withDayOfMonth(1);
            LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
            
            // 转换为日期时间格式
            LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();
            LocalDateTime endOfMonth = lastDayOfMonth.atTime(23, 59, 59);
            
            // 构建查询条件
            LambdaQueryWrapper<ReportItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReportItem::getDoctorId, doctorId)
                   .ge(ReportItem::getCreateTime, startOfMonth)
                   .le(ReportItem::getCreateTime, endOfMonth);
            
            // 统计符合条件的记录数
            Long count = reportItemMapper.selectCount(wrapper);
            log.info("医生[{}]本月体检报告数量：{}", doctorId, count);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.error("统计医生本月体检报告数量出错", e);
            return 0;
        }
    }
    
    @Override
    public ApiResult getPendingReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]待处理报告列表", doctorId);
        try {
            // 查询条件：待处理报告定义为conclusion为空的报告
            LambdaQueryWrapper<ReportItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReportItem::getDoctorId, doctorId)
                   .isNull(ReportItem::getConclusion)
                   .or()
                   .eq(ReportItem::getConclusion, "")
                   .orderByDesc(ReportItem::getCreateTime); // 按创建时间降序排序
            
            // 分页查询
            Page<ReportItem> page = new Page<>(1, 50); // 默认查询第一页，每页50条
            Page<ReportItem> reportPage = reportItemMapper.selectPage(page, wrapper);
            
            // 封装返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("records", reportPage.getRecords());
            result.put("total", reportPage.getTotal());
            result.put("pages", reportPage.getPages());
            result.put("current", reportPage.getCurrent());
            result.put("size", reportPage.getSize());
            
            return ApiResult.success(result);
        } catch (Exception e) {
            log.error("获取医生待处理报告列表出错", e);
            return ApiResult.fail("获取待处理报告列表失败：" + e.getMessage());
        }
    }
} 