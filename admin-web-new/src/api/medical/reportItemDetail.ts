import {http} from '@/utils/http'

// 添加体检报告检查项明细
export function addReportItemDetail(data:any) {
    return http.post<any>('/admin/reportItemDetail/addReportItemDetail', data)
}

// 修改体检报告检查项明细
export function updateReportItemDetail(data:any) {
    return http.post<any>('/admin/reportItemDetail/updateReportItemDetail', data)
}

// 删除体检报告检查项明细
export function deleteReportItemDetail(id:string) {
    return http.post<any>('/admin/reportItemDetail/deleteReportItemDetail/'+id)
}

// 获取体检报告检查项明细详情
export function getReportItemDetail(id:any) {
    return http.post<any>('/admin/reportItemDetail/getReportItemDetail/'+id)
}

// 获取体检报告检查项明细分页列表
export function getReportItemDetailPage(data:any) {
    return http.post<any>('/admin/reportItemDetail/getReportItemDetailPage', data)
}





















