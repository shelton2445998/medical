package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Hospital;
import com.fourth.medical.medical.query.HospitalQuery;
import com.fourth.medical.medical.vo.HospitalVo;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.vo.AppHospitalVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 医院 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {

    /**
     * 医院详情
     *
     * @param id
     * @return
     */
    HospitalVo getHospitalById(Long id);

    /**
     * 医院分页列表
     *
     * @param query
     * @return
     */
    List<HospitalVo> getHospitalPage(HospitalQuery query);

    /**
     * App医院详情
     *
     * @param id
     * @return
     */
    AppHospitalVo getAppHospitalById(Long id);

    /**
     * App医院分页列表
     *
     * @param query
     * @return
     */
    List<AppHospitalVo> getAppHospitalPage(AppHospitalQuery query);

}
