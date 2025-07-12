package com.fourth.medical.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.constant.SystemConstant;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.system.dto.SysMenuDto;
import com.fourth.medical.system.entity.SysMenu;
import com.fourth.medical.system.enums.SysMenuType;
import com.fourth.medical.system.mapper.SysMenuMapper;
import com.fourth.medical.system.query.SysMenuQuery;
import com.fourth.medical.system.service.SysMenuService;
import com.fourth.medical.system.service.SysRoleMenuService;
import com.fourth.medical.system.vo.SysMenuTreeVo;
import com.fourth.medical.system.vo.SysMenuVo;
import com.fourth.medical.system.vo.SysNavMenuTreeVo;
import com.fourth.medical.util.ObjectValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统菜单 服务实现类
 *
 * @author geekidea
 * @since 2022-12-26
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addSysMenu(SysMenuDto dto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(dto, sysMenu);
        Long parentId = dto.getParentId();
        if (parentId == null) {
            sysMenu.setParentId(SystemConstant.ROOT_MENU_ID);
        }
        return save(sysMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysMenu(SysMenuDto dto) {
        Long id = dto.getId();
        SysMenu sysMenu = getById(id);
        if (sysMenu == null) {
            throw new BusinessException("系统菜单不存在");
        }
        BeanUtils.copyProperties(dto, sysMenu);
        return updateById(sysMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysMenu(Long id) {
        // 获取菜单
        SysMenu sysMenu = getById(id);
        if (sysMenu == null) {
            throw new BusinessException("菜单数据不存在");
        }
        // 所有关联菜单ID集合
        List<Long> deleteMenuIds = new ArrayList<>();
        deleteMenuIds.add(id);
        Integer type = sysMenu.getType();
        SysMenuType sysMenuType = SysMenuType.get(type);
        if (SysMenuType.DIR == sysMenuType) {
            // 如果是目录，则先查询所有的子菜单ID，再根据子菜单ID查询所有的权限菜单ID
            List<Long> menuIds = sysMenuMapper.getChildrenMenuIds(Arrays.asList(id));
            deleteMenuIds.addAll(menuIds);
            if (CollectionUtils.isNotEmpty(menuIds)) {
                List<Long> permissionMenuIds = sysMenuMapper.getChildrenMenuIds(menuIds);
                deleteMenuIds.addAll(permissionMenuIds);
            }
        } else if (SysMenuType.MENU == sysMenuType) {
            // 如果是菜单，则查询所有的权限子菜单
            List<Long> menuIds = sysMenuMapper.getChildrenMenuIds(Arrays.asList(id));
            if (CollectionUtils.isNotEmpty(menuIds)) {
                deleteMenuIds.addAll(menuIds);
            }
        }
        // 删除角色菜单关系表中的相关菜单关联
        sysRoleMenuService.deleteSysRoleMenuByMenuIds(deleteMenuIds);
        // 删除所有关联菜单
        return removeByIds(deleteMenuIds);
    }

    @Override
    public SysMenuVo getSysMenuById(Long id) {
        return sysMenuMapper.getSysMenuById(id);
    }

    @Override
    public List<SysMenuTreeVo> getAllSysMenuTreeList(SysMenuQuery query) {
        List<SysMenuTreeVo> list = sysMenuMapper.getSysMenuTreeList(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 如果搜索条件有值，则直接返回普通列表
        boolean flag = ObjectValueUtil.isHaveValue(query);
        if (flag) {
            return list;
        }
        // 递归返回树形列表
        return recursionSysMenuTreeList(0L, list);
    }

    @Override
    public List<SysMenuTreeVo> getSysMenuTreeList() {
        SysMenuQuery query = new SysMenuQuery();
        query.setStatus(true);
        List<SysMenuTreeVo> list = sysMenuMapper.getSysMenuTreeList(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 递归返回树形列表
        return recursionSysMenuTreeList(0L, list);
    }

    @Override
    public List<SysNavMenuTreeVo> getNavMenuTreeList() {
        LoginVo loginVo = LoginUtil.getLoginVo();
        if (loginVo == null) {
            log.warn("获取菜单时用户未登录");
            return new ArrayList<>();
        }
        
        Long userId = loginVo.getUserId();
        log.info("获取用户[{}]的导航菜单", userId);
        
        // 如果是管理员，则查询所有可用菜单，否则获取当前用户所有可用的菜单
        boolean isAdmin = LoginUtil.isAdmin();
        List<SysNavMenuTreeVo> list;
        
        // 检查是否是医生角色（可以根据实际情况修改判断条件）
        boolean isDoctor = loginVo.getRoleId() != null && 
                          (loginVo.getRoleId() == 2L || // 假设2是医生角色ID
                           "doctor".equals(loginVo.getRoleName())); // 假设医生角色名为doctor
        
        if (isAdmin) {
            log.info("用户是管理员，获取所有菜单");
            list = sysMenuMapper.getNavMenuTreeAllList();
        } else if (isDoctor) {
            log.info("用户是医生，获取医生专属菜单");
            // 这里可以添加特殊逻辑，比如过滤只有医生可见的菜单
            list = sysMenuMapper.getNavMenuTreeList(userId);
            // 如果需要，可以在这里添加医生特有的菜单项
        } else {
            log.info("普通用户，获取用户菜单");
            list = sysMenuMapper.getNavMenuTreeList(userId);
        }
        
        log.info("获取到的菜单数量: {}", list != null ? list.size() : 0);
        
        // 递归返回树形列表
        return recursionSysNavMenuTreeList(0L, list);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return sysMenuMapper.getMenuIdsByRoleId(roleId);
    }

    /**
     * 递归设置树形菜单
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<SysMenuTreeVo> recursionSysMenuTreeList(Long parentId, List<SysMenuTreeVo> list) {
        return list.stream()
                .filter(vo -> vo.getParentId().equals(parentId))
                .map(item -> {
                    item.setChildren(recursionSysMenuTreeList(item.getId(), list));
                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * 递归设置树形导航菜单
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<SysNavMenuTreeVo> recursionSysNavMenuTreeList(Long parentId, List<SysNavMenuTreeVo> list) {
        return list.stream()
                .filter(vo -> vo.getParentId().equals(parentId))
                .map(item -> {
                    item.setChildren(recursionSysNavMenuTreeList(item.getId(), list));
                    return item;
                })
                .collect(Collectors.toList());
    }

}
