import {http} from '@/utils/http'

// 添加系统通知
export function addNotification(data:any) {
    return http.post<any>('/admin/notification/addNotification', data)
}

// 修改系统通知
export function updateNotification(data:any) {
    return http.post<any>('/admin/notification/updateNotification', data)
}

// 删除系统通知
export function deleteNotification(id:string) {
    return http.post<any>('/admin/notification/deleteNotification/'+id)
}

// 获取系统通知详情
export function getNotification(id:any) {
    return http.post<any>('/admin/notification/getNotification/'+id)
}

// 获取系统通知分页列表
export function getNotificationPage(data:any) {
    return http.post<any>('/admin/notification/getNotificationPage', data)
}





















