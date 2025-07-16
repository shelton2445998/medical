package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 医生排班 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {

    /**
     * 医生排班详情
     *
     * @param id
     * @return
     */
    DoctorScheduleVo getDoctorScheduleById(Long id);

    /**
     * 医生排班分页列表
     *
     * @param query
     * @return
     */
    List<DoctorScheduleVo> getDoctorSchedulePage(DoctorScheduleQuery query);
    
    /**
     * 根据医生ID和日期范围获取医生排班列表
     *
     * @param doctorId 医生ID
     * @param query 查询条件
     * @return 医生排班列表
     */
    List<DoctorScheduleVo> getDoctorScheduleListByDoctorId(@Param("doctorId") Long doctorId, @Param("query") DoctorScheduleQuery query);

    /**
     * App医生排班详情
     *
     * @param id
     * @return
     */
    AppDoctorScheduleVo getAppDoctorScheduleById(Long id);

    /**
     * App医生排班分页列表
     *
     * @param query
     * @return
     */
    List<AppDoctorScheduleVo> getAppDoctorSchedulePage(AppDoctorScheduleQuery query);

    /**
     * 查询检查项所属科室ID列表
     *
     * @param checkItemIds 检查项ID列表
     * @return 科室ID列表
     */
    @Select("<script>" +
            "SELECT DISTINCT department_id FROM checkitem " +
            "WHERE id IN " +
            "<foreach collection='checkItemIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Long> getCheckItemDepartments(@Param("checkItemIds") List<Long> checkItemIds);

    /**
     * 查询检查项与科室的映射关系
     *
     * @param checkItemIds 检查项ID列表
     * @return 检查项ID和科室ID的映射列表
     */
    @Select("<script>" +
            "SELECT id AS checkItemId, department_id AS departmentId FROM checkitem " +
            "WHERE id IN " +
            "<foreach collection='checkItemIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Map<String, Object>> getCheckItemDepartmentMapping(@Param("checkItemIds") List<Long> checkItemIds);

    /**
     * 查询当天值班的医生列表，并按照当天预约人数排序
     *
     * @param hospitalId 医院ID
     * @param scheduleDate 排班日期
     * @param departmentIds 科室ID列表
     * @return 医生列表及其预约数量
     */
    List<Map<String, Object>> getAvailableDoctorsForSchedule(
            @Param("hospitalId") Long hospitalId,
            @Param("scheduleDate") Date scheduleDate,
            @Param("departmentIds") List<Long> departmentIds);

    /**
     * 查询当天值班的医生列表，并按照当天预约人数排序（按科室）
     *
     * @param hospitalId 医院ID
     * @param scheduleDate 排班日期
     * @param departmentId 科室ID
     * @return 医生列表及其预约数量
     */
    @Select("SELECT " +
            "d.id AS doctorId, " +
            "d.name AS doctorName, " +
            "COUNT(o.id) AS appointmentCount " +
            "FROM doctor d " +
            "JOIN doctor_schedule ds ON d.id = ds.doctor_id " +
            "LEFT JOIN orders o ON d.id = o.doctor_id AND DATE(o.appointment_date) = DATE(#{scheduleDate}) " +
            "WHERE d.hospital_id = #{hospitalId} " +
            "AND d.department_id = #{departmentId} " +
            "AND d.status = 1 " +
            "AND DATE(ds.work_date) = DATE(#{scheduleDate}) " +
            "GROUP BY d.id, d.name " +
            "ORDER BY appointmentCount")
    List<Map<String, Object>> getAvailableDoctorsForDepartment(
            @Param("hospitalId") Long hospitalId,
            @Param("scheduleDate") Date scheduleDate,
            @Param("departmentId") Long departmentId);

    /**
     * 查询最近排班的医生列表（包括过去和未来的最近排班）
     *
     * @param hospitalId 医院ID
     * @param referenceDate 参考日期
     * @param departmentIds 科室ID列表
     * @return 医生列表及其排班信息，按照与参考日期差值排序
     */
    List<Map<String, Object>> getNearestAvailableDoctors(
            @Param("hospitalId") Long hospitalId,
            @Param("referenceDate") Date referenceDate,
            @Param("departmentIds") List<Long> departmentIds);

    /**
     * 查询最近排班的医生列表（按科室）
     *
     * @param hospitalId 医院ID
     * @param referenceDate 参考日期
     * @param departmentId 科室ID
     * @return 医生列表及其排班信息，按照与参考日期差值排序
     */
    @Select("SELECT " +
            "d.id AS doctorId, " +
            "d.name AS doctorName, " +
            "ds.work_date AS workDate, " +
            "ABS(DATEDIFF(ds.work_date, #{referenceDate})) AS dateDiff " +
            "FROM doctor d " +
            "JOIN doctor_schedule ds ON d.id = ds.doctor_id " +
            "WHERE d.hospital_id = #{hospitalId} " +
            "AND d.department_id = #{departmentId} " +
            "AND d.status = 1 " +
            "ORDER BY dateDiff " +
            "LIMIT 1")
    List<Map<String, Object>> getNearestAvailableDoctorsForDepartment(
            @Param("hospitalId") Long hospitalId,
            @Param("referenceDate") Date referenceDate,
            @Param("departmentId") Long departmentId);

    /**
     * 更新订单的医生ID
     *
     * @param orderId 订单ID
     * @param doctorId 医生ID
     * @return 受影响的行数
     */
    @Update("UPDATE orders SET doctor_id = #{doctorId}, update_time = NOW() WHERE id = #{orderId}")
    int updateOrderDoctor(@Param("orderId") Long orderId, @Param("doctorId") Long doctorId);
}
