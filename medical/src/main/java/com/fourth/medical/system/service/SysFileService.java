package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysFileDto;
import com.fourth.medical.system.entity.SysFile;
import com.fourth.medical.system.query.SysFileQuery;
import com.fourth.medical.system.vo.SysFileVo;


/**
 * 系统文件 服务接口
 *
 * @author geekidea
 * @since 2023-11-26
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 修改系统文件
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysFile(SysFileDto dto);

    /**
     * 删除系统文件
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysFile(Long id);

    /**
     * 系统文件详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysFileVo getSysFileById(Long id);

    /**
     * 系统文件分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysFileVo> getSysFilePage(SysFileQuery query);

}
