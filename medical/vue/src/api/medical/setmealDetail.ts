import {http} from '@/utils/http'

// 添加体检套餐明细
export function addSetmealDetail(data:any) {
    return http.post<any>('/admin/setmealDetail/addSetmealDetail', data)
}

// 修改体检套餐明细
export function updateSetmealDetail(data:any) {
    return http.post<any>('/admin/setmealDetail/updateSetmealDetail', data)
}

// 删除体检套餐明细
export function deleteSetmealDetail(id:string) {
    return http.post<any>('/admin/setmealDetail/deleteSetmealDetail/'+id)
}

// 获取体检套餐明细详情
export function getSetmealDetail(id:any) {
    return http.post<any>('/admin/setmealDetail/getSetmealDetail/'+id)
}

// 获取体检套餐明细分页列表
export function getSetmealDetailPage(data:any) {
    return http.post<any>('/admin/setmealDetail/getSetmealDetailPage', data)
}





















