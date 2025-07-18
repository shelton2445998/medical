/**
 * 系统配置API接口模块
 * 提供系统配置参数的CRUD操作功能
 */
import {http} from '@/utils/http'
import {ListResultType, ResultType} from "@/api/types";

/**
 * 系统配置API接口枚举
 * 定义所有系统配置相关的API端点
 */
enum Api {
    updateSysConfig = '/admin/sysConfig/updateSysConfig',   // 更新系统配置
    getSysConfigPage = '/admin/sysConfig/getSysConfigPage', // 获取系统配置分页列表
    getSysConfig = '/admin/sysConfig/getSysConfig/',        // 获取系统配置详情
    deleteSysConfig = '/admin/sysConfig/deleteSysConfig/',  // 删除系统配置
    addSysConfig = '/admin/sysConfig/addSysConfig',         // 添加系统配置
}

/**
 * 系统配置管理相关接口
 */

/**
 * 修改系统配置
 * @param data 系统配置数据
 * @returns 返回修改操作结果
 */
export function updateSysConfigApi(data:any) {
    return http.post<ResultType>(Api.updateSysConfig,data)
}

/**
 * 获取系统配置分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回系统配置列表和分页信息
 */
export function getSysConfigPageApi(data:any) {
    return http.post<ListResultType>(Api.getSysConfigPage, data)
}

/**
 * 获取系统配置详情
 * @param id 配置ID
 * @returns 返回指定配置的详细信息
 */
export function getSysConfigApi(id:string|number) {
    return http.post<any>(Api.getSysConfig+id)
}

/**
 * 删除系统配置
 * @param id 配置ID
 * @returns 返回删除操作结果
 */
export function deleteSysConfigApi(id:string|number) {
    return http.post<any>(Api.deleteSysConfig+id)
}

/**
 * 添加系统配置
 * @param data 系统配置数据
 * @returns 返回添加操作结果
 */
export function addSysConfigApi(data:any) {
    return http.post<any>(Api.addSysConfig,data)
}










