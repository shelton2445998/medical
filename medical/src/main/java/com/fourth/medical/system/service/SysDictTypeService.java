package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.system.dto.SysDictTypeDto;
import com.fourth.medical.system.entity.SysDictType;
import com.fourth.medical.system.query.SysDictTypeQuery;
import com.fourth.medical.system.vo.SysDictTypeVo;

import java.util.List;


/**
 * 字典类型 服务接口
 *
 * @author geekidea
 * @since 2023-11-25
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 添加字典类型
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysDictType(SysDictTypeDto dto);

    /**
     * 修改字典类型
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysDictType(SysDictTypeDto dto);

    /**
     * 删除字典类型
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysDictType(Long id);

    /**
     * 字典类型详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysDictTypeVo getSysDictTypeById(Long id);

    /**
     * 字典类型列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    List<SysDictTypeVo> getSysDictTypeList(SysDictTypeQuery query);

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     * @throws Exception
     */
    void checkCodeExists(String code);

}
