package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorDto;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.query.DoctorQuery;
import com.fourth.medical.medical.vo.DoctorVo;
import com.fourth.medical.medical.query.AppDoctorQuery;
import com.fourth.medical.medical.vo.AppDoctorVo;


/**
 * 医生 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface DoctorService extends IService<Doctor> {

    /**
     * 添加医生
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addDoctor(DoctorDto dto);

    /**
     * 修改医生
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateDoctor(DoctorDto dto);

    /**
     * 删除医生
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteDoctor(Long id);

    /**
     * 医生详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    DoctorVo getDoctorById(Long id);

    /**
     * 医生分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<DoctorVo> getDoctorPage(DoctorQuery query);

    /**
     * App医生详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppDoctorVo getAppDoctorById(Long id);

    /**
     * App医生分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppDoctorVo> getAppDoctorPage(AppDoctorQuery query);

}
