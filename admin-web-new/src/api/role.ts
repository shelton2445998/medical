/**
 * 角色管理API接口模块
 * 提供角色相关的CRUD操作和权限管理功能
 */
import {http} from '@/utils/http'

/**
 * 角色管理API接口枚举
 * 定义所有角色相关的API端点
 */
enum Api {
    updateSysRole = '/admin/sysRole/updateSysRole',         // 更新角色信息
    getSysRoleAllList = '/admin/sysRole/getSysRoleAllList', // 获取所有角色列表
    getMenuIdsByRoleId = '/admin/sysRole/getMenuIdsByRoleId/', // 获取角色权限菜单
    setRoleMenus = '/admin/sysRole/setRoleMenus',           // 设置角色权限
    getSysRoleList = '/admin/sysRole/getSysRolePage',       // 获取角色分页列表
    getSysRole = '/admin/sysRole/getSysRole/',              // 获取角色详情
    deleteSysRole = '/admin/sysRole/deleteSysRole/',        // 删除角色
    addSysRole = '/admin/sysRole/addSysRole',               // 添加角色
}

/**
 * 系统角色管理相关接口
 */

/**
 * 获取角色分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回角色列表和分页信息
 */
export function getSysRoleList(data:any) {
    return http.post<any>(Api.getSysRoleList, data)
}

/**
 * 获取系统所有角色列表
 * @param data 查询参数
 * @returns 返回系统中所有角色的列表
 */
export function getSysRoleAllList(data:any) {
    return http.post<any>(Api.getSysRoleAllList, data)
}

/**
 * 获取角色权限菜单ID列表
 * @param id 角色ID
 * @returns 返回该角色拥有的菜单ID列表
 */
export function getMenuIdsByRoleId(id:any) {
    return http.post<any>(Api.getMenuIdsByRoleId+id)
}

/**
 * 设置角色权限
 * @param data 角色权限数据，包含角色ID和菜单ID列表
 * @returns 返回设置权限操作结果
 */
export function setRoleMenus(data:any) {
    return http.post<any>(Api.setRoleMenus, data)
}

/**
 * 添加角色
 * @param data 角色信息数据
 * @returns 返回添加操作结果
 */
export function addSysRole(data:any) {
    return http.post<any>(Api.addSysRole,data)
}

/**
 * 获取角色详情
 * @param id 角色ID
 * @returns 返回指定角色的详细信息
 */
export function getSysRole(id:string|number) {
    return http.post<any>(Api.getSysRole+id)
}

/**
 * 修改角色信息
 * @param data 角色信息数据
 * @returns 返回修改操作结果
 */
export function updateSysRole(data:any) {
    return http.post<any>(Api.updateSysRole,data)
}

/**
 * 删除角色
 * @param id 角色ID
 * @returns 返回删除操作结果
 */
export function deleteSysRole(id:string|number) {
    return http.post<any>(Api.deleteSysRole+id)
}














