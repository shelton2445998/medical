package com.fourth.medical.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.system.entity.SysUser;
import com.fourth.medical.system.query.SysUserQuery;
import com.fourth.medical.system.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户 Mapper 接口
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 系统用户详情
     *
     * @param id
     * @return
     */
    SysUserVo getSysUserById(Long id);

    /**
     * 系统用户分页列表
     *
     * @param query
     * @return
     */
    List<SysUserVo> getSysUserPage(SysUserQuery query);

    /**
     * 根据用户名获取登录用户对象
     *
     * @param username
     * @return
     */
    SysUser getSysUserByUsername(String username);

    /**
     * 根据角色ID获取用户数量
     *
     * @param roleId
     * @return
     */
    Integer getCountByRoleId(Long roleId);

}
