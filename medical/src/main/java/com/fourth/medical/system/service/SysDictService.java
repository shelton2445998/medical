package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysDictDto;
import com.fourth.medical.system.entity.SysDict;
import com.fourth.medical.system.query.SysDictAppQuery;
import com.fourth.medical.system.query.SysDictQuery;
import com.fourth.medical.system.vo.AppSysDictVo;
import com.fourth.medical.system.vo.SysDictVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 字典数据 服务接口
 *
 * @author geekidea
 * @since 2023-11-25
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 添加字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysDict(SysDictDto dto);

    /**
     * 修改字典数据
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysDict(SysDictDto dto);

    /**
     * 删除字典数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysDict(Long id);

    /**
     * 字典数据详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysDictVo getSysDictById(Long id);

    /**
     * 字典数据分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysDictVo> getSysDictPage(SysDictQuery query);

    /**
     * App字典数据列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Map<String, List<AppSysDictVo>> getAppSysDictList(SysDictAppQuery query);

    /**
     * 根据字典编码获取字典列表
     *
     * @param dictCode
     * @return
     * @throws Exception
     */
    List<SysDict> getSysDictList(String dictCode);

    /**
     * 根据字典编码和label获取字典对象
     *
     * @param dictCode
     * @param value
     * @return
     * @throws Exception
     */
    SysDict getSysDictByValue(String dictCode, Serializable value);

    /**
     * 根据字典编码和label获取字典值
     *
     * @param dictCode
     * @param value
     * @return
     * @throws Exception
     */
    String getSysDictLabelByValue(String dictCode, Serializable value);

}
