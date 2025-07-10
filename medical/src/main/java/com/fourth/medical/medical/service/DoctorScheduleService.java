package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorScheduleDto;
import com.fourth.medical.medical.entity.DoctorSchedule;
import com.fourth.medical.medical.query.DoctorScheduleQuery;
import com.fourth.medical.medical.vo.DoctorScheduleVo;
import com.fourth.medical.medical.query.AppDoctorScheduleQuery;
import com.fourth.medical.medical.vo.AppDoctorScheduleVo;


/**
 * 医生排班 服务接口
 *
 * @author fourth
 * @since 2025-07-09
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

}
