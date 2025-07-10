package com.fourth.medical.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.generator.entity.GeneratorColumn;
import com.fourth.medical.generator.vo.GeneratorColumnDbVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生成代码列 Mapper 接口
 *
 * @author geekidea
 * @since 2023-12-29
 */
@Mapper
public interface GeneratorColumnMapper extends BaseMapper<GeneratorColumn> {

    /**
     * 获取数据库列集合
     *
     * @param tableName
     * @return
     */
    List<GeneratorColumnDbVo> getDbColumnListByTableName(String tableName);

    /**
     * 根据表名称获取生成列集合
     *
     * @param tableName
     * @return
     */
    List<GeneratorColumn> getGeneratorColumnList(String tableName);

}
