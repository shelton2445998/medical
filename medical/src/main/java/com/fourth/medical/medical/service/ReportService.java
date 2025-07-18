package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.ReportDto;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.query.ReportQuery;
import com.fourth.medical.medical.query.AppReportQuery;
import com.fourth.medical.medical.vo.ReportVo;
import com.fourth.medical.medical.vo.AppReportVo;

/**
 * 体检报告总 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供体检报告管理的核心业务逻辑，包括报告的创建、修改、查询、生成等</li>
 *   <li>支持体检报告的完整生命周期管理，为医疗系统提供报告数据服务</li>
 *   <li>提供移动端和Web端统一的体检报告数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>报告管理：支持体检报告的创建、编辑、删除和查询</li>
 *   <li>自动生成：支持根据订单自动生成体检报告</li>
 *   <li>权限控制：基于用户令牌的数据访问控制</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 *   <li>移动端适配：提供专门的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>报告创建：根据体检订单自动创建体检报告</li>
 *   <li>报告生成：根据检查项结果生成完整的体检报告</li>
 *   <li>报告查询：用户查询自己的体检报告</li>
 *   <li>报告维护：医生或管理员维护体检报告信息</li>
 *   <li>权限验证：验证用户对报告的访问权限</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于用户令牌的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>隐私保护：保护用户体检报告的隐私信息</li>
 *   <li>数据完整性：确保体检报告数据的准确性和完整性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>报告模板：可扩展支持自定义报告模板功能</li>
 *   <li>报告导出：可扩展支持报告PDF导出功能</li>
 *   <li>报告分享：可扩展支持报告分享功能</li>
 *   <li>智能分析：可扩展支持基于报告数据的智能分析</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-14
 */
public interface ReportService extends IService<Report> {

    /**
     * 添加体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addReport(ReportDto dto);

    /**
     * 修改体检报告总
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateReport(ReportDto dto);

    /**
     * 删除体检报告总
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteReport(Long id);

    /**
     * 体检报告总详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    ReportVo getReportById(Long id);

    /**
     * 体检报告总分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<ReportVo> getReportPage(ReportQuery query);

    /**
     * App体检报告总详情
     *
     * @param id
     * @param token 用户令牌
     * @return
     * @throws Exception
     */
    AppReportVo getAppReportById(Long id, String token);

    /**
     * App体检报告总分页列表
     *
     * @param query
     * @param token 用户令牌
     * @return
     * @throws Exception
     */
    Paging<AppReportVo> getAppReportPage(AppReportQuery query, String token);

    /**
     * 为订单创建体检报告
     *
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param checkitemIds 检查项ID列表，以逗号分隔
     * @return 报告ID
     */
    Long createReportForOrder(Long orderId, Long userId, String checkitemIds);
}
