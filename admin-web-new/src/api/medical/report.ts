import {http} from '@/utils/http'

// 添加体检报告总
export function addReport(data:any) {
    return http.post<any>('/admin/report/addReport', data)
}

// 修改体检报告总
export function updateReport(data:any) {
    return http.post<any>('/admin/report/updateReport', data)
}

// 删除体检报告总
export function deleteReport(id:string) {
    return http.post<any>('/admin/report/deleteReport/'+id)
}

// 获取体检报告总详情
export function getReport(id:any) {
    return http.post<any>('/admin/report/getReport/'+id)
}

// 获取体检报告总分页列表
export function getReportPage(data:any) {
    return http.post<any>('/admin/report/getReportPage', data)
}





















