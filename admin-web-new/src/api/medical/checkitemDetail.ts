import {http} from '@/utils/http'

// 添加检查项明细
export function addCheckitemDetail(data:any) {
    return http.post<any>('/admin/checkitemDetail/addCheckitemDetail', data)
}

// 修改检查项明细
export function updateCheckitemDetail(data:any) {
    return http.post<any>('/admin/checkitemDetail/updateCheckitemDetail', data)
}

// 删除检查项明细
export function deleteCheckitemDetail(id:string) {
    return http.post<any>('/admin/checkitemDetail/deleteCheckitemDetail/'+id)
}

// 获取检查项明细详情
export function getCheckitemDetail(id:any) {
    return http.post<any>('/admin/checkitemDetail/getCheckitemDetail/' + id)
}

// 获取检查项明细分页列表
export function getCheckitemDetailPage(data:any) {
    return http.post<any>('/admin/checkitemDetail/getCheckitemDetailPage', data)
}





















