package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportItemDto;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.query.ReportItemQuery;
import com.fourth.medical.medical.vo.ReportItemVo;
import com.fourth.medical.medical.query.AppReportItemQuery;
import com.fourth.medical.medical.vo.AppReportItemVo;

import java.util.List;


/**
 * 体检报告检查项信息 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface ReportItemService extends IService<ReportItem> {

    /**
     * 添加体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addReportItem(ReportItemDto dto);

    /**
     * 修改体检报告检查项信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateReportItem(ReportItemDto dto);

    /**
     * 删除体检报告检查项信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReportItem(Long id);

    /**
     * 体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    ReportItemVo getReportItemById(Long id);

    /**
     * 体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<ReportItemVo> getReportItemPage(ReportItemQuery query);

    /**
     * App体检报告检查项信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppReportItemVo getAppReportItemById(Long id);

    /**
     * App体检报告检查项信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppReportItemVo> getAppReportItemPage(AppReportItemQuery query);
    
    /**
     * 根据用户ID获取体检报告列表
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<ReportItemVo> getReportItemsByUserId(Long userId);

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
    List<Long> createReportItemsForReport(Long reportId, Long orderId, Long userId, String checkitemIds, Long doctorId);
    
    /**
     * 更新报告项的医生ID
     *
     * @param reportItemId 报告项ID
     * @param doctorId 医生ID
     * @return 是否更新成功
     */
    boolean updateReportItemDoctor(Long reportItemId, Long doctorId);

    /**
     * 获取医生待处理报告列表
     * 待处理报告定义为conclusion字段为空的报告
     *
     * @param doctorId 医生ID
     * @return 待处理报告列表
     */
    ApiResult getPendingReportsByDoctorId(Long doctorId);
}
