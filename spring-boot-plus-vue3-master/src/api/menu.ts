import {http} from '@/utils/http'
import {getToken} from '@/utils/auth'

enum Api {
    // 管理员端API
    getAllSysMenuTreeList = '/admin/sysMenu/getAllSysMenuTreeList',
    getSysMenuTreeList = '/admin/sysMenu/getSysMenuTreeList',
    getAdminNavMenuTreeList = '/admin/sysMenu/getNavMenuTreeList',
    addSysMenu = '/admin/sysMenu/addSysMenu',
    getSysMenu = '/admin/sysMenu/getSysMenu/',
    updateSysMenu = '/admin/sysMenu/updateSysMenu',
    deleteSysMenu = '/admin/sysMenu/deleteSysMenu/',
    // 医生端API
    getDoctorNavMenuTreeList = '/doctor/sysMenu/getNavMenuTreeList',
}

/** 菜单*/
// 获取所有的系统菜单树形列表
export function getAllSysMenuTreeList(data:any) {
    return http.post<any>(Api.getAllSysMenuTreeList, data)
}

// 获取当前用户的导航菜单
export function getNavMenuTreeList(data:any) {
    // 根据token判断是管理员还是医生
    const tokenData = getToken();
    const token = tokenData ? tokenData.token : '';
    console.log('当前token:', token);
    
    // 如果token以boot.doctor开头，则使用医生API，否则使用管理员API
    if (token && token.startsWith('boot.doctor')) {
        console.log('使用医生导航菜单API');
        return http.post<any>(Api.getDoctorNavMenuTreeList, data)
    } else {
        console.log('使用管理员导航菜单API');
        return http.post<any>(Api.getAdminNavMenuTreeList, data)
    }
}

// 获取启用的系统菜单树形列表
export function getSysMenuTreeList(data:any) {
    return http.post<any>(Api.getSysMenuTreeList, data)
}
// 添加菜单
export function addSysMenu(data:any) {
    return http.post<any>(Api.addSysMenu,data)
}
// 菜单详情
export function getSysMenu(id:string|number) {
    return http.post<any>(Api.getSysMenu+id)
}
// 修改菜单
export function updateSysMenu(data:any) {
    return http.post<any>(Api.updateSysMenu,data)
}
// 删除菜单
export function deleteSysMenu(id:string|number) {
    return http.post<any>(Api.deleteSysMenu+id)
}














