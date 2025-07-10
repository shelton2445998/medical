package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

}
