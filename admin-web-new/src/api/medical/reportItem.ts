import {http} from '@/utils/http'

// 添加体检报告检查项信息
export function addReportItem(data:any) {
    return http.post<any>('/admin/reportItem/addReportItem', data)
}

// 修改体检报告检查项信息
export function updateReportItem(data:any) {
    return http.post<any>('/admin/reportItem/updateReportItem', data)
}

// 删除体检报告检查项信息
export function deleteReportItem(id:string) {
    return http.post<any>('/admin/reportItem/deleteReportItem/'+id)
}

// 获取体检报告检查项信息详情
export function getReportItem(id:any) {
    return http.post<any>('/admin/reportItem/getReportItem/'+id)
}

// 获取体检报告检查项信息分页列表
export function getReportItemPage(data:any) {
    return http.post<any>('/admin/reportItem/getReportItemPage', data)
}





















