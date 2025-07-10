import {http} from '@/utils/http'

// 添加体检用户信息
export function addUser(data:any) {
    return http.post<any>('/admin/user/addUser', data)
}

// 修改体检用户信息
export function updateUser(data:any) {
    return http.post<any>('/admin/user/updateUser', data)
}

// 删除体检用户信息
export function deleteUser(id:string) {
    return http.post<any>('/admin/user/deleteUser/'+id)
}

// 获取体检用户信息详情
export function getUser(id:any) {
    return http.post<any>('/admin/user/getUser/'+id)
}

// 获取体检用户信息分页列表
export function getUserPage(data:any) {
    return http.post<any>('/admin/user/getUserPage', data)
}





















