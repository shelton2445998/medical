package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.mapper.ReportItemMapper;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;
import com.fourth.medical.util.PageUtil;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fourth.medical.framework.response.ApiResult;
import java.util.Map;
import java.util.HashMap;

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
        // 创建MyBatis-Plus分页对象
        IPage<ReportItemVo> page = PageUtil.buildPage(query);
        // 执行分页查询，获取结果列表
        List<ReportItemVo> list = reportItemMapper.getReportItemList(page, query);
        // 将结果封装到Paging对象中
        Paging<ReportItemVo> paging = new Paging<>();
        paging.setList(list);
        paging.setTotal(page.getTotal());
        paging.setPageIndex(query.getPageIndex());
        paging.setPageSize(query.getPageSize());
        return paging;
    }

    @Override
    public AppReportItemVo getAppReportItemById(Long id) {
        return reportItemMapper.getAppReportItemById(id);
    }
    
    @Override
    public ApiResult getPendingReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]待处理报告列表", doctorId);
        try {
            // 查询条件：待处理报告定义为conclusion为空的报告
            LambdaQueryWrapper<ReportItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReportItem::getDoctorId, doctorId)
                   .and(w -> w.isNull(ReportItem::getConclusion)
                           .or()
                           .eq(ReportItem::getConclusion, ""))
                   .orderByDesc(ReportItem::getCreateTime); // 按创建时间降序排序
            
            // 分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ReportItem> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 50); // 默认查询第一页，每页50条
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ReportItem> reportPage = page(page, wrapper);
            
            // 转换为VO
            List<ReportItemVo> reportItemVos = reportPage.getRecords().stream()
                .map(item -> {
                    ReportItemVo vo = reportItemMapper.getReportItemById(item.getId());
                    if (vo == null) {
                        vo = new ReportItemVo();
                        BeanUtils.copyProperties(item, vo);
                    }
                    return vo;
                })
                .collect(Collectors.toList());
            
            // 封装返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("records", reportItemVos);
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

    /**
     * 为体检报告创建检查项记录
     *
     * @param reportId 报告ID
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param checkitemIds 检查项ID列表，以逗号分隔
     * @param doctorId 医生ID
     * @return 创建的检查项ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createReportItemsForReport(Long reportId, Long orderId, Long userId, String checkitemIds, Long doctorId) {
        log.info("为体检报告创建检查项，报告ID：{}，订单ID：{}，用户ID：{}，检查项：{}，医生ID：{}", 
                reportId, orderId, userId, checkitemIds, doctorId);
        
        List<Long> createdItemIds = new ArrayList<>();
        
        try {
            // 解析检查项ID列表
            String[] itemIds = checkitemIds.split(",");
            
            for (String itemIdStr : itemIds) {
                Long itemId = Long.parseLong(itemIdStr.trim());
                
                // 检查是否已经存在该检查项的报告
                List<ReportItem> existingItems = baseMapper.selectList(
                        new LambdaQueryWrapper<ReportItem>()
                                .eq(ReportItem::getOrderId, orderId)
                                .eq(ReportItem::getItemId, itemId));
                
                if (existingItems != null && !existingItems.isEmpty()) {
                    // 如果有多条记录，取第一条
                    ReportItem existingItem = existingItems.get(0);
                    log.info("检查项报告已存在，ID：{}", existingItem.getId());
                    createdItemIds.add(existingItem.getId());
                    continue;
                }
                
                // 创建新的报告检查项
                ReportItem reportItem = new ReportItem();
                reportItem.setOrderId(orderId);
                reportItem.setUserId(userId);
                reportItem.setItemId(itemId);
                reportItem.setDoctorId(doctorId);
                reportItem.setReportStatus(0); // 未生成状态
                
                // 插入数据库
                baseMapper.insert(reportItem);
                log.info("成功创建检查项报告，ID：{}", reportItem.getId());
                
                createdItemIds.add(reportItem.getId());
            }
            
            // 更新report表中的report_item_ids字段
            if (!createdItemIds.isEmpty()) {
                String reportItemIdsStr = createdItemIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                
                baseMapper.updateReportItemIds(reportId, reportItemIdsStr);
            }
            
            return createdItemIds;
        } catch (Exception e) {
            log.error("创建检查项报告失败", e);
            throw new BusinessException("创建检查项报告失败");
        }
    }
    
    /**
     * 更新报告项的医生ID
     *
     * @param reportItemId 报告项ID
     * @param doctorId 医生ID
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReportItemDoctor(Long reportItemId, Long doctorId) {
        log.info("更新报告项医生ID，报告项ID：{}，医生ID：{}", reportItemId, doctorId);
        
        try {
            // 获取报告项
            ReportItem reportItem = getById(reportItemId);
            if (reportItem == null) {
                log.error("报告项不存在，ID：{}", reportItemId);
                return false;
            }
            
            // 如果医生ID已经被设置且不为0，则不需要更新
            if (reportItem.getDoctorId() != null && reportItem.getDoctorId() != 0) {
                log.info("报告项已有负责医生，不需要更新，报告项ID：{}，当前医生ID：{}", 
                        reportItemId, reportItem.getDoctorId());
                return true;
            }
            
            // 更新医生ID
            LambdaUpdateWrapper<ReportItem> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ReportItem::getId, reportItemId)
                         .set(ReportItem::getDoctorId, doctorId);
            
            boolean result = update(updateWrapper);
            if (result) {
                log.info("成功更新报告项医生ID，报告项ID：{}，医生ID：{}", reportItemId, doctorId);
            } else {
                log.error("更新报告项医生ID失败，报告项ID：{}，医生ID：{}", reportItemId, doctorId);
            }
            
            return result;
        } catch (Exception e) {
            log.error("更新报告项医生ID失败", e);
            throw new BusinessException("更新报告项医生ID失败");
        }
    }
    
    @Override
    public ApiResult getCompletedReportsByDoctorId(Long doctorId) {
        log.info("获取医生[{}]已完成报告列表", doctorId);
        try {
            // 查询条件：已完成报告定义为conclusion不为空且不为空字符串的报告
            LambdaQueryWrapper<ReportItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ReportItem::getDoctorId, doctorId)
                   .isNotNull(ReportItem::getConclusion)
                   .ne(ReportItem::getConclusion, "")
                   .orderByDesc(ReportItem::getCreateTime); // 按创建时间降序排序
            
            // 分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ReportItem> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 50); // 默认查询第一页，每页50条
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<ReportItem> reportPage = page(page, wrapper);
            
            // 转换为VO
            List<ReportItemVo> reportItemVos = reportPage.getRecords().stream()
                .map(item -> {
                    ReportItemVo vo = reportItemMapper.getReportItemById(item.getId());
                    if (vo == null) {
                        vo = new ReportItemVo();
                        BeanUtils.copyProperties(item, vo);
                    }
                    return vo;
                })
                .collect(Collectors.toList());
            
            // 封装返回结果，保持与待处理报告相同的格式
            Map<String, Object> result = new HashMap<>();
            result.put("records", reportItemVos);
            result.put("total", reportPage.getTotal());
            result.put("pages", reportPage.getPages());
            result.put("current", reportPage.getCurrent());
            result.put("size", reportPage.getSize());
            
            return ApiResult.success(result);
        } catch (Exception e) {
            log.error("获取医生已完成报告列表出错", e);
            return ApiResult.fail("获取已完成报告列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 为单个检查项创建报告记录
     *
     * @param reportId 报告ID
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param checkItemId 检查项ID
     * @param doctorId 医生ID
     * @return 创建的检查项报告ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReportItemForCheckItem(Long reportId, Long orderId, Long userId, Long checkItemId, Long doctorId) {
        log.info("为单个检查项创建报告记录，报告ID：{}，订单ID：{}，用户ID：{}，检查项ID：{}，医生ID：{}", 
                reportId, orderId, userId, checkItemId, doctorId);
        
        try {
            // 检查是否已经存在该检查项的报告
            List<ReportItem> existingItems = baseMapper.selectList(
                    new LambdaQueryWrapper<ReportItem>()
                            .eq(ReportItem::getOrderId, orderId)
                            .eq(ReportItem::getItemId, checkItemId));
            
            if (existingItems != null && !existingItems.isEmpty()) {
                // 如果有多条记录，取第一条
                ReportItem existingItem = existingItems.get(0);
                log.info("检查项报告已存在，ID：{}", existingItem.getId());
                return existingItem.getId();
            }
            
            // 创建新的报告检查项
            ReportItem reportItem = new ReportItem();
            reportItem.setOrderId(orderId);
            reportItem.setUserId(userId);
            reportItem.setItemId(checkItemId);
            reportItem.setDoctorId(doctorId);
            reportItem.setReportId(reportId);
            reportItem.setReportStatus(0); // 未生成状态
            
            // 插入数据库
            baseMapper.insert(reportItem);
            log.info("成功创建检查项报告，ID：{}", reportItem.getId());
            
            return reportItem.getId();
        } catch (Exception e) {
            log.error("创建检查项报告失败", e);
            throw new BusinessException("创建检查项报告失败");
        }
    }
}
