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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供体检报告检查项管理的核心业务逻辑，包括检查项的创建、修改、查询、状态管理等</li>
 *   <li>支持体检报告检查项的完整生命周期管理，为医生工作台提供数据服务</li>
 *   <li>提供移动端和Web端统一的检查项报告数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>检查项管理：支持体检报告检查项的创建、编辑、删除和查询</li>
 *   <li>自动创建：支持根据体检报告自动创建检查项记录</li>
 *   <li>医生分配：支持检查项与医生的关联分配</li>
 *   <li>状态管理：支持检查项的待处理和已完成状态管理</li>
 *   <li>批量操作：支持批量创建和管理检查项记录</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>检查项创建：根据体检报告自动创建检查项记录</li>
 *   <li>医生分配：将检查项分配给相应的医生</li>
 *   <li>状态跟踪：跟踪检查项的处理状态</li>
 *   <li>结果录入：医生录入检查结果和结论</li>
 *   <li>报告生成：根据检查项结果生成完整报告</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>数据完整性：确保检查项数据的准确性和完整性</li>
 *   <li>关联保护：保护检查项与报告、医生关联关系的完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>工作流管理：可扩展支持检查项工作流管理</li>
 *   <li>质量监控：可扩展支持检查项质量监控功能</li>
 *   <li>统计分析：可扩展支持医生工作效率统计分析</li>
 *   <li>智能分配：可扩展支持基于医生专长的智能分配</li>
 * </ul>
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
     * 为单个检查项创建报告记录
     *
     * @param reportId 报告ID
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param checkItemId 检查项ID
     * @param doctorId 医生ID
     * @return 创建的检查项报告ID
     */
    Long createReportItemForCheckItem(Long reportId, Long orderId, Long userId, Long checkItemId, Long doctorId);
    
    /**
     * 更新报告项的医生ID
     *
     * @param reportItemId 报告项ID
     * @param doctorId 医生ID
     * @return 是否更新成功
     */
    boolean updateReportItemDoctor(Long reportItemId, Long doctorId);
    
    /**
     * 更新report表中的report_item_ids字段
     *
     * @param reportId 报告ID
     * @param reportItemIds 报告项ID列表字符串
     * @return 是否更新成功
     */
    boolean updateReportItemIds(Long reportId, String reportItemIds);

    /**
     * 获取医生待处理报告列表
     * 待处理报告定义为conclusion字段为空的报告
     *
     * @param doctorId 医生ID
     * @return 待处理报告列表
     */
    ApiResult getPendingReportsByDoctorId(Long doctorId);
    
    /**
     * 获取医生已完成报告列表
     * 已完成报告定义为conclusion字段不为空的报告
     *
     * @param doctorId 医生ID
     * @return 已完成报告列表
     */
    ApiResult getCompletedReportsByDoctorId(Long doctorId);
}
