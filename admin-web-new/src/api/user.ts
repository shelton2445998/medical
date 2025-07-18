/**
 * 用户管理API接口模块
 * 提供用户相关的CRUD操作和认证功能
 */
import {http} from '@/utils/http'
import {
    GetSysUserListParamsType,
    GetSysUserListResult,
    updatePasswordParamsType,
    updateUserParamsType,
} from "@/api/types/userTypes";
import {ListResultType, ResultType} from "@/api/types";

/**
 * 用户管理API接口枚举
 * 定义所有用户相关的API端点
 */
enum Api {
    updateSysUser = '/admin/user/update',           // 更新用户信息
    getSysUserList = '/admin/user/list',            // 获取用户列表
    getSysUser = '/admin/user/detail/',             // 获取用户详情
    deleteSysUser = '/admin/user/delete/',          // 删除用户
    addSysUser = '/admin/user/add',                 // 添加用户
    resetSysUserPassword = '/admin/user/reset-password', // 重置用户密码
    updatePassword = '/admin/user/update-password',  // 修改密码
    updateProfile = '/admin/user/update-profile',   // 修改个人信息
    getProfile = '/admin/user/profile',             // 获取个人信息
}

/**
 * 系统用户管理相关接口
 */

/**
 * 获取用户分页列表
 * @param data 查询参数，包含分页信息和筛选条件
 * @returns 返回用户列表和分页信息
 */
export function getSysUserList(data: GetSysUserListParamsType) {
    return http.get<ListResultType>(Api.getSysUserList, data)
}

/**
 * 获取用户详情
 * @param id 用户ID
 * @returns 返回指定用户的详细信息
 */
export function getSysUser(id: string | number) {
    return http.get<ResultType>(Api.getSysUser + id)
}

/**
 * 删除用户
 * @param id 用户ID
 * @returns 返回删除操作结果
 */
export function deleteSysUser(id: string | number) {
    return http.post<ResultType>(Api.deleteSysUser + id)
}

/**
 * 添加用户
 * @param data 用户信息数据
 * @returns 返回添加操作结果
 */
export function addSysUser(data: updateUserParamsType) {
    return http.post<ResultType>(Api.addSysUser, data)
}

/**
 * 修改用户信息
 * @param data 用户信息数据
 * @returns 返回修改操作结果
 */
export function updateSysUser(data: updateUserParamsType) {
    return http.post<ResultType>(Api.updateSysUser, data)
}

/**
 * 重置用户密码
 * @param data 重置密码所需的参数
 * @returns 返回重置操作结果
 */
export function resetSysUserPassword(data: any) {
    return http.post<ResultType>(Api.resetSysUserPassword, data)
}

/**
 * 修改当前用户密码
 * @param data 密码修改参数，包含原密码和新密码
 * @returns 返回修改操作结果
 */
export function updatePassword(data: updatePasswordParamsType) {
    return http.post<ResultType>(Api.updatePassword, data)
}

/**
 * 修改个人信息
 * @param data 个人信息数据
 * @returns 返回修改操作结果
 */
export function updateProfile(data: updateUserParamsType) {
    return http.post<ResultType>(Api.updateProfile, data)
}

/**
 * 获取当前用户个人信息
 * @returns 返回当前用户的个人信息
 */
export function getProfile() {
    return http.get<ResultType>(Api.getProfile)
}
