package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.query.DoctorQuery;
import com.fourth.medical.medical.vo.DoctorVo;
import com.fourth.medical.medical.query.AppDoctorQuery;
import com.fourth.medical.medical.vo.AppDoctorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 医生 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    /**
     * 医生详情
     *
     * @param id
     * @return
     */
    DoctorVo getDoctorById(Long id);

    /**
     * 医生分页列表
     *
     * @param query
     * @return
     */
    List<DoctorVo> getDoctorPage(DoctorQuery query);

    /**
     * App医生详情
     *
     * @param id
     * @return
     */
    AppDoctorVo getAppDoctorById(Long id);

    /**
     * App医生分页列表
     *
     * @param query
     * @return
     */
    List<AppDoctorVo> getAppDoctorPage(AppDoctorQuery query);

}
