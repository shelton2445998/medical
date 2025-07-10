package com.fourth.medical.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.system.entity.SysConfig;
import com.fourth.medical.system.query.SysConfigQuery;
import com.fourth.medical.system.vo.SysConfigVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统配置 Mapper 接口
 *
 * @author geekidea
 * @since 2023-11-27
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 系统配置详情
     *
     * @param id
     * @return
     */
    SysConfigVo getSysConfigById(Long id);

    /**
     * 系统配置分页列表
     *
     * @param query
     * @return
     */
    List<SysConfigVo> getSysConfigPage(SysConfigQuery query);

}
