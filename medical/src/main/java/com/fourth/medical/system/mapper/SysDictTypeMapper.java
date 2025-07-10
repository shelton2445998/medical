package com.fourth.medical.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.system.entity.SysDictType;
import com.fourth.medical.system.query.SysDictTypeQuery;
import com.fourth.medical.system.vo.SysDictTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典类型 Mapper 接口
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    /**
     * 字典类型详情
     *
     * @param id
     * @return
     */
    SysDictTypeVo getSysDictTypeById(Long id);

    /**
     * 字典类型列表
     *
     * @param query
     * @return
     */
    List<SysDictTypeVo> getSysDictTypeList(SysDictTypeQuery query);

}
