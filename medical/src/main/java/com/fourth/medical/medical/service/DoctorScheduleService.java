package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorScheduleDto;
import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 医生排班服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供医生排班管理的核心业务逻辑，包括排班的创建、修改、查询、智能分配等</li>
 *   <li>支持医生排班的完整生命周期管理，为预约系统提供排班数据服务</li>
 *   <li>提供移动端和Web端统一的医生排班数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>排班管理：支持医生排班的创建、编辑、删除和查询</li>
 *   <li>智能分配：支持根据检查项和科室智能分配医生</li>
 *   <li>预约统计：支持医生预约数量的统计和查询</li>
 *   <li>时间管理：支持按日期范围查询排班信息</li>
 *   <li>移动端适配：提供专门的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>排班创建：管理员为医生创建排班计划</li>
 *   <li>智能分配：根据预约信息自动分配合适的医生</li>
 *   <li>排班查询：查询医生的排班和预约情况</li>
 *   <li>统计分析：统计医生的预约数量和工作量</li>
 *   <li>状态管理：管理排班的状态和可用性</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>数据完整性：确保排班数据的准确性和完整性</li>
 *   <li>冲突检测：防止排班时间冲突和重复分配</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>智能调度：可扩展支持更复杂的智能调度算法</li>
 *   <li>工作负载均衡：可扩展支持医生工作负载均衡</li>
 *   <li>排班优化：可扩展支持排班优化建议</li>
 *   <li>预警系统：可扩展支持排班冲突预警功能</li>
 * </ul>
 *
 * @author fourth
 * @since 2023-07-09
 */
public interface DoctorScheduleService extends IService<DoctorSchedule> {

    /**
     * 添加医生排班
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addDoctorSchedule(DoctorScheduleDto dto);

    /**
     * 修改医生排班
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateDoctorSchedule(DoctorScheduleDto dto);

    /**
     * 删除医生排班
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteDoctorSchedule(Long id);

    /**
     * 医生排班详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    DoctorScheduleVo getDoctorScheduleById(Long id);

    /**
     * 医生排班分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<DoctorScheduleVo> getDoctorSchedulePage(DoctorScheduleQuery query);
    
    /**
     * 根据医生ID和日期范围获取医生排班列表
     *
     * @param doctorId 医生ID
     * @param query 查询条件
     * @return 医生排班列表
     */
    List<DoctorScheduleVo> getDoctorScheduleListByDoctorId(Long doctorId, DoctorScheduleQuery query);

    /**
     * App医生排班详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppDoctorScheduleVo getAppDoctorScheduleById(Long id);

    /**
     * App医生排班分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppDoctorScheduleVo> getAppDoctorSchedulePage(AppDoctorScheduleQuery query);

    /**
     * 根据订单信息和检查项目分配医生
     *
     * @param orderId 订单ID
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param checkitemIds 检查项ID列表
     * @return 分配的医生ID，如果没有合适的医生则返回null
     */
    Long assignDoctorForOrder(Long orderId, Long hospitalId, Date appointmentDate, String checkitemIds);

    /**
     * 检查医生今日是否有排班
     *
     * @param doctorId 医生ID
     * @return 今日是否有排班
     */
    Boolean checkDoctorHasScheduleToday(Long doctorId);

    /**
     * 获取医生今日预约数量
     *
     * @param doctorId 医生ID
     * @return 今日预约数量
     */
    Integer countTodayAppointmentsByDoctorId(Long doctorId);

    /**
     * 获取医生本周预约数量
     *
     * @param doctorId 医生ID
     * @return 本周预约数量
     */
    Integer countWeekAppointmentsByDoctorId(Long doctorId);
    
    /**
     * 获取检查项与科室的映射关系
     *
     * @param checkItemIds 检查项ID列表
     * @return 检查项ID到科室ID的映射
     */
    Map<Long, Long> getCheckItemDepartmentMap(List<Long> checkItemIds);
    
    /**
     * 根据科室分配医生
     *
     * @param hospitalId 医院ID
     * @param appointmentDate 预约日期
     * @param departmentIds 科室ID列表
     * @return 科室ID到医生ID的映射
     */
    Map<Long, Long> assignDoctorsForDepartments(Long hospitalId, Date appointmentDate, List<Long> departmentIds);
}
