import {http} from '@/utils/http'

// 添加医生
export function addDoctor(data:any) {
    return http.post<any>('/admin/doctor/addDoctor', data)
}

// 修改医生
export function updateDoctor(data:any) {
    return http.post<any>('/admin/doctor/updateDoctor', data)
}

// 删除医生
export function deleteDoctor(id:string) {
    return http.post<any>('/admin/doctor/deleteDoctor/'+id)
}

// 获取医生详情
export function getDoctor(id:any) {
    return http.post<any>('/admin/doctor/getDoctor/'+id)
}

// 获取医生分页列表
export function getDoctorPage(data:any) {
    return http.post<any>('/admin/doctor/getDoctorPage', data)
}





















