import {http} from '@/utils/http'

// 添加医生排班
export function addDoctorSchedule(data:any) {
    return http.post<any>('/admin/doctorSchedule/addDoctorSchedule', data)
}

// 修改医生排班
export function updateDoctorSchedule(data:any) {
    return http.post<any>('/admin/doctorSchedule/updateDoctorSchedule', data)
}

// 删除医生排班
export function deleteDoctorSchedule(id:string) {
    return http.post<any>('/admin/doctorSchedule/deleteDoctorSchedule/'+id)
}

// 获取医生排班详情
export function getDoctorSchedule(id:any) {
    return http.post<any>('/admin/doctorSchedule/getDoctorSchedule/'+id)
}

// 获取医生排班分页列表
export function getDoctorSchedulePage(data:any) {
    return http.post<any>('/admin/doctorSchedule/getDoctorSchedulePage', data)
}





















