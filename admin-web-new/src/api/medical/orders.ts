import {http} from '@/utils/http'

// 添加体检预约订单
export function addOrders(data:any) {
    return http.post<any>('/admin/orders/addOrders', data)
}

// 修改体检预约订单
export function updateOrders(data:any) {
    return http.post<any>('/admin/orders/updateOrders', data)
}

// 删除体检预约订单
export function deleteOrders(id:string) {
    return http.post<any>('/admin/orders/deleteOrders/'+id)
}

// 获取体检预约订单详情
export function getOrders(id:any) {
    return http.get<any>('/admin/appointment/detail/' + id)
}

// 获取体检预约订单分页列表
export function getOrdersPage(data:any) {
    return http.get<any>('/admin/appointment/list', data)
}





















