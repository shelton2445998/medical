import {http} from '@/utils/http'

// 添加体检套餐
export function addSetmeal(data:any) {
    return http.post<any>('/admin/setmeal/addSetmeal', data)
}

// 修改体检套餐
export function updateSetmeal(data:any) {
    return http.post<any>('/admin/setmeal/updateSetmeal', data)
}

// 删除体检套餐
export function deleteSetmeal(id:string) {
    return http.post<any>('/admin/setmeal/deleteSetmeal/'+id)
}

// 获取体检套餐详情
export function getSetmeal(id:any) {
    return http.get<any>('/admin/package/detail/' + id)
}

// 获取体检套餐分页列表
export function getSetmealPage(data:any) {
    return http.get<any>('/admin/package/list', data)
}





















