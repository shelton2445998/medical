/**
 * 菜单管理API接口模块
 * 提供系统菜单的CRUD操作和树形结构管理功能
 */
import {http} from '@/utils/http'

/**
 * 菜单管理API接口枚举
 * 定义所有菜单相关的API端点
 */
enum Api {
    getAllSysMenuTreeList = '/admin/sysMenu/getAllSysMenuTreeList',  // 获取所有系统菜单树
    getSysMenuTreeList = '/admin/sysMenu/getSysMenuTreeList',        // 获取启用的系统菜单树
    getNavMenuTreeList = '/admin/sysMenu/getNavMenuTreeList',        // 获取导航菜单树
    addSysMenu = '/admin/sysMenu/addSysMenu',                        // 添加系统菜单
    getSysMenu = '/admin/sysMenu/getSysMenu/',                       // 获取菜单详情
    updateSysMenu = '/admin/sysMenu/updateSysMenu',                  // 更新系统菜单
    deleteSysMenu = '/admin/sysMenu/deleteSysMenu/',                 // 删除系统菜单
}

/**
 * 系统菜单管理相关接口
 */

/**
 * 获取所有的系统菜单树形列表
 * @param data 查询参数
 * @returns 返回完整的系统菜单树形结构
 */
export function getAllSysMenuTreeList(data:any) {
    return http.post<any>(Api.getAllSysMenuTreeList, data)
}

/**
 * 获取当前用户的导航菜单树
 * @param data 查询参数
 * @returns 返回当前用户可访问的导航菜单树
 */
export function getNavMenuTreeList(data:any) {
    return http.post<any>(Api.getNavMenuTreeList, data)
}

/**
 * 获取启用的系统菜单树形列表
 * @param data 查询参数
 * @returns 返回启用状态的系统菜单树形结构
 */
export function getSysMenuTreeList(data:any) {
    return http.post<any>(Api.getSysMenuTreeList, data)
}

/**
 * 添加系统菜单
 * @param data 菜单信息数据
 * @returns 返回添加操作结果
 */
export function addSysMenu(data:any) {
    return http.post<any>(Api.addSysMenu,data)
}

/**
 * 获取菜单详情
 * @param id 菜单ID
 * @returns 返回指定菜单的详细信息
 */
export function getSysMenu(id:string|number) {
    return http.post<any>(Api.getSysMenu+id)
}

/**
 * 修改系统菜单
 * @param data 菜单信息数据
 * @returns 返回修改操作结果
 */
export function updateSysMenu(data:any) {
    return http.post<any>(Api.updateSysMenu,data)
}

/**
 * 删除系统菜单
 * @param id 菜单ID
 * @returns 返回删除操作结果
 */
export function deleteSysMenu(id:string|number) {
    return http.post<any>(Api.deleteSysMenu+id)
}














