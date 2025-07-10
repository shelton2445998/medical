package com.fourth.medical.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.generator.entity.GeneratorTable;
import com.fourth.medical.generator.query.GeneratorTableQuery;
import com.fourth.medical.generator.vo.GeneratorTableDbVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生成代码表 Mapper 接口
 *
 * @author geekidea
 * @since 2023-12-29
 */
@Mapper
public interface GeneratorTableMapper extends BaseMapper<GeneratorTable> {

    /**
     * 生成代码表详情
     *
     * @param tableName
     * @return
     */
    GeneratorTable getGeneratorTableByTableName(String tableName);

    /**
     * 根据表名称获取表信息
     *
     * @param tableName
     * @return
     */
    GeneratorTableDbVo getDbTableByTableName(String tableName);

    /**
     * 数据库中的表分页列表
     *
     * @param query
     * @return
     */
    List<GeneratorTableDbVo> getDbTablePage(GeneratorTableQuery query);

}
