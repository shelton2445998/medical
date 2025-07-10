package com.fourth.medical.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.generator.dto.GeneratorColumnDto;
import com.fourth.medical.generator.entity.GeneratorColumn;
import com.fourth.medical.generator.mapper.GeneratorColumnMapper;
import com.fourth.medical.generator.service.GeneratorColumnService;
import com.fourth.medical.generator.util.GeneratorUtil;
import com.fourth.medical.generator.vo.GeneratorColumnDbVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 生成代码列 服务实现类
 *
 * @author geekidea
 * @since 2023-12-29
 */
@Slf4j
@Service
public class GeneratorColumnServiceImpl extends ServiceImpl<GeneratorColumnMapper, GeneratorColumn> implements GeneratorColumnService {

    @Autowired
    private GeneratorColumnMapper generatorColumnMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addGeneratorColumn(String tableName, Boolean validateField) {
        List<GeneratorColumnDbVo> columnDbVos = getDbColumnListByTableName(tableName);
        if (CollectionUtils.isEmpty(columnDbVos)) {
            throw new BusinessException("没有列信息");
        }
        List<GeneratorColumn> columns = GeneratorUtil.getGeneratorColumns(tableName, columnDbVos, validateField);
        return saveBatch(columns);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGeneratorColumn(GeneratorColumnDto dto) {
        Long id = dto.getId();
        GeneratorColumn generatorColumn = getById(id);
        if (generatorColumn == null) {
            throw new BusinessException("生成代码列不存在");
        }
        BeanUtils.copyProperties(dto, generatorColumn);
        return updateById(generatorColumn);
    }

    @Override
    public List<GeneratorColumnDbVo> getDbColumnListByTableName(String tableName) {
        return generatorColumnMapper.getDbColumnListByTableName(tableName);
    }

    @Override
    public List<GeneratorColumn> getGeneratorColumnList(String tableName) {
        return generatorColumnMapper.getGeneratorColumnList(tableName);
    }

}
