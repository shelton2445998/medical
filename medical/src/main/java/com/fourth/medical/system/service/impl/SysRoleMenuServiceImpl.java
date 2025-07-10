package com.fourth.medical.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.system.entity.SysRoleMenu;
import com.fourth.medical.system.mapper.SysRoleMenuMapper;
import com.fourth.medical.system.service.SysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单关系表 服务实现类
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public void deleteSysRoleMenuByRoleId(Long roleId) {
        LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        remove(wrapper);
    }

    @Override
    public void deleteSysRoleMenuByMenuIds(List<Long> menuIds) {
        LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(SysRoleMenu::getMenuId, menuIds);
        remove(wrapper);
    }

}
