import {http} from '@/utils/http'
import {
    GetSysUserListParamsType,
    GetSysUserListResult,
    updatePasswordParamsType,
    updateUserParamsType,
} from "@/api/types/userTypes";
import {ListResultType, ResultType} from "@/api/types";

enum Api {
    updateSysUser = '/admin/user/update',
    getSysUserList = '/admin/user/list',
    getSysUser = '/admin/user/detail/',
    deleteSysUser = '/admin/user/delete/',
    addSysUser = '/admin/user/add',
    resetSysUserPassword = '/admin/user/reset-password',
    updatePassword = '/admin/user/update-password',
    updateProfile = '/admin/user/update-profile',
    getProfile = '/admin/user/profile',
}


/** 系统用户*/
// 用户分页列表
export function getSysUserList(data: GetSysUserListParamsType) {
    return http.get<ListResultType>(Api.getSysUserList, data)
}

// 用户详情
export function getSysUser(id: string | number) {
    return http.get<ResultType>(Api.getSysUser + id)
}

// 删除用户
export function deleteSysUser(id: string | number) {
    return http.post<ResultType>(Api.deleteSysUser + id)
}

// 添加用户
export function addSysUser(data: updateUserParamsType) {
    return http.post<ResultType>(Api.addSysUser, data)
}

// 修改用户
export function updateSysUser(data: updateUserParamsType) {
    return http.post<ResultType>(Api.updateSysUser, data)
}

// 重置密码
export function resetSysUserPassword(data: any) {
    return http.post<ResultType>(Api.resetSysUserPassword, data)
}

// 修改密码
export function updatePassword(data: updatePasswordParamsType) {
    return http.post<ResultType>(Api.updatePassword, data)
}

// 修改个人信息
export function updateProfile(data: updateUserParamsType) {
    return http.post<ResultType>(Api.updateProfile, data)
}

// 获取个人信息
export function getProfile() {
    return http.get<ResultType>(Api.getProfile)
}
