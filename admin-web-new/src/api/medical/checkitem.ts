import {http} from '@/utils/http'

// 添加检查项
export function addCheckitem(data:any) {
    return http.post<any>('/admin/checkitem/addCheckitem', data)
}

// 修改检查项
export function updateCheckitem(data:any) {
    return http.post<any>('/admin/checkitem/updateCheckitem', data)
}

// 删除检查项
export function deleteCheckitem(id:string) {
    return http.post<any>('/admin/checkitem/deleteCheckitem/'+id)
}

// 获取检查项详情
export function getCheckitem(id:any) {
    return http.post<any>('/admin/checkitem/getCheckitem/'+id)
}

// 获取检查项分页列表
export function getCheckitemPage(data:any) {
    return http.post<any>('/admin/checkitem/getCheckitemPage', data)
}





















