import {http} from '@/utils/http'

// 添加总检结论
export function addOverallResult(data:any) {
    return http.post<any>('/admin/overallResult/addOverallResult', data)
}

// 修改总检结论
export function updateOverallResult(data:any) {
    return http.post<any>('/admin/overallResult/updateOverallResult', data)
}

// 删除总检结论
export function deleteOverallResult(id:string) {
    return http.post<any>('/admin/overallResult/deleteOverallResult/'+id)
}

// 获取总检结论详情
export function getOverallResult(id:any) {
    return http.post<any>('/admin/overallResult/getOverallResult/'+id)
}

// 获取总检结论分页列表
export function getOverallResultPage(data:any) {
    return http.post<any>('/admin/overallResult/getOverallResultPage', data)
}





















