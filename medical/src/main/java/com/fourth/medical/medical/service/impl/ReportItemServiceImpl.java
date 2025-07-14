package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.mapper.ReportItemMapper;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.service.ReportItemService;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;
import com.fourth.medical.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

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
        return reportItemMapper.getReportItemPage(PageUtil.buildPage(query), query);
    }

    @Override
    public AppReportItemVo getAppReportItemById(Long id) {
        return reportItemMapper.getAppReportItemById(id);
    }

    @Override
    public Paging<AppReportItemVo> getAppReportItemPage(AppReportItemQuery query) {
        return reportItemMapper.getAppReportItemPage(PageUtil.buildPage(query), query);
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
                ReportItem existingItem = baseMapper.selectOne(
                        new LambdaQueryWrapper<ReportItem>()
                                .eq(ReportItem::getOrderId, orderId)
                                .eq(ReportItem::getItemId, itemId));
                
                if (existingItem != null) {
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
}
