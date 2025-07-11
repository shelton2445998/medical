package com.fourth.medical.generator.service.impl;

import com.fourth.medical.generator.entity.GeneratorTable;
import com.fourth.medical.generator.service.GeneratorService;
import com.fourth.medical.generator.service.GeneratorTableService;
import com.fourth.medical.generator.util.GeneratorUtil;
import com.fourth.medical.generator.vo.GeneratorCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author geekidea
 * @date 2023/12/31
 **/
@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private GeneratorTableService generatorTableService;

    @Override
    public List<GeneratorCodeVo> previewCode(String tableName) {
        GeneratorTable generatorTable = generatorTableService.getGeneratorTableInfo(tableName);
        return GeneratorUtil.generatorCodeData(tableName, generatorTable);
    }

    @Override
    public void generatorCode(List<String> tableNames) throws Exception {
        for (String tableName : tableNames) {
            GeneratorTable generatorTable = generatorTableService.getGeneratorTableInfo(tableName);
            GeneratorUtil.generatorCode(tableName, generatorTable);
        }
    }

    @Override
    public Map<String, List<GeneratorCodeVo>> downloadCode(List<String> tableNames) {
        Map<String, List<GeneratorCodeVo>> map = new LinkedHashMap<>();
        for (String tableName : tableNames) {
            GeneratorTable generatorTable = generatorTableService.getGeneratorTableInfo(tableName);
            List<GeneratorCodeVo> generatorCodeVos = GeneratorUtil.generatorCodeData(tableName, generatorTable);
            map.put(tableName, generatorCodeVos);
        }
        return map;
    }

}
