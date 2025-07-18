/**
 * 字典管理API接口模块
 * 提供字典数据和字典类型的CRUD操作功能
 */
import {http} from '@/utils/http'
import {ListResultType, ResultType} from "@/api/types";

/**
 * 字典管理API接口枚举
 * 定义所有字典相关的API端点
 */
enum Api {
    // 字典数据相关接口
    updateSysDict = '/admin/sysDict/updateSysDict',         // 更新字典数据
    getSysDictPage = '/admin/sysDict/getSysDictPage',       // 获取字典数据分页列表
    getSysDict = '/admin/sysDict/getSysDict/',              // 获取字典数据详情
    deleteSysDict = '/admin/sysDict/deleteSysDict/',        // 删除字典数据
    addSysDict = '/admin/sysDict/addSysDict',               // 添加字典数据

    // 字典类型相关接口
    updateSysDictType = '/admin/sysDictType/updateSysDictType',     // 更新字典类型
    getSysDictTypeList = '/admin/sysDictType/getSysDictTypeList',   // 获取字典类型列表
    getSysDictType = '/admin/sysDictType/getSysDictType/',          // 获取字典类型详情
    deleteSysDictType = '/admin/sysDictType/deleteSysDictType/',    // 删除字典类型
    addSysDictType = '/admin/sysDictType/addSysDictType',           // 添加字典类型
}

/**
 * 字典数据管理相关接口
 */

/**
 * 修改字典数据
 * @param data 字典数据信息
 * @returns 返回修改操作结果
 */
export function updateSysDictApi(data:any) {
    return http.post<ResultType>(Api.updateSysDict,data)
}

/**
 * 获取字典数据分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回字典数据列表和分页信息
 */
export function getSysDictPageApi(data:any) {
    return http.post<ListResultType>(Api.getSysDictPage, data)
}

/**
 * 获取字典数据详情
 * @param id 字典数据ID
 * @returns 返回指定字典数据的详细信息
 */
export function getSysDictApi(id:string|number) {
    return http.post<any>(Api.getSysDict+id)
}

/**
 * 删除字典数据
 * @param id 字典数据ID
 * @returns 返回删除操作结果
 */
export function deleteSysDictApi(id:string|number) {
    return http.post<any>(Api.deleteSysDict+id)
}

/**
 * 添加字典数据
 * @param data 字典数据信息
 * @returns 返回添加操作结果
 */
export function addSysDictApi(data:any) {
    return http.post<any>(Api.addSysDict,data)
}

/**
 * 字典类型管理相关接口
 */

/**
 * 修改字典类型
 * @param data 字典类型信息
 * @returns 返回修改操作结果
 */
export function updateSysDictTypeApi(data:any) {
    return http.post<ResultType>(Api.updateSysDictType,data)
}

/**
 * 获取字典类型分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回字典类型列表和分页信息
 */
export function getSysDictTypeListApi(data:any) {
    return http.post<any>(Api.getSysDictTypeList, data)
}

/**
 * 获取字典类型详情
 * @param id 字典类型ID
 * @returns 返回指定字典类型的详细信息
 */
export function getSysDictTypeApi(id:string):Promise<any> {
    return http.post<any>(Api.getSysDictType+id)
}

/**
 * 删除字典类型
 * @param id 字典类型ID
 * @returns 返回删除操作结果
 */
export function deleteSysDictTypeApi(id:string) {
    return http.post<any>(Api.deleteSysDictType+id)
}

/**
 * 添加字典类型
 * @param data 字典类型信息
 * @returns 返回添加操作结果
 */
export function addSysDictTypeApi(data:any) {
    return http.post<any>(Api.addSysDictType,data)
}










