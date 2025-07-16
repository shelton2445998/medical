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
