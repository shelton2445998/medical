package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.HospitalDto;
import com.fourth.medical.medical.entity.Hospital;
import com.fourth.medical.medical.query.HospitalQuery;
import com.fourth.medical.medical.vo.HospitalVo;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.vo.AppHospitalVo;

import java.util.List;


/**
 * 医院 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface HospitalService extends IService<Hospital> {

    /**
     * 添加医院
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addHospital(HospitalDto dto);

    /**
     * 修改医院
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateHospital(HospitalDto dto);

    /**
     * 删除医院
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteHospital(Long id);

    /**
     * 医院详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    HospitalVo getHospitalById(Long id);

    /**
     * 医院分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<HospitalVo> getHospitalPage(HospitalQuery query);

    /**
     * App医院详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppHospitalVo getAppHospitalById(Long id);

    /**
     * App医院分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppHospitalVo> getAppHospitalPage(AppHospitalQuery query);
    
    /**
     * 获取推荐医院列表
     *
     * @return
     * @throws Exception
     */
    List<AppHospitalVo> getRecommendHospitals();

}
