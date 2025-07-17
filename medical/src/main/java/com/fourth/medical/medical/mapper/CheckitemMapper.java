package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Checkitem;
import com.fourth.medical.medical.query.CheckitemQuery;
import com.fourth.medical.medical.vo.CheckitemVo;
import com.fourth.medical.medical.query.AppCheckitemQuery;
import com.fourth.medical.medical.vo.AppCheckitemVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 检查项 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface CheckitemMapper extends BaseMapper<Checkitem> {

    /**
     * 检查项详情
     *
     * @param id
     * @return
     */
    CheckitemVo getCheckitemById(Long id);

    /**
     * 检查项分页列表
     *
     * @param query
     * @return
     */
    List<CheckitemVo> getCheckitemPage(CheckitemQuery query);

    /**
     * App检查项详情
     *
     * @param id
     * @return
     */
    AppCheckitemVo getAppCheckitemById(Long id);

    /**
     * App检查项分页列表
     *
     * @param query
     * @return
     */
    List<AppCheckitemVo> getAppCheckitemPage(AppCheckitemQuery query);

    /**
     * 根据检查项ID列表获取部门信息
     *
     * @param checkitemIds 检查项ID列表
     * @return 部门信息
     */
    Object getDepartmentByCheckitemIds(List<Long> checkitemIds);

}
