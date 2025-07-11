import {http} from '@/utils/http'

// 添加医院
export function addHospital(data:any) {
    return http.post<any>('/admin/hospital/addHospital', data)
}

// 修改医院
export function updateHospital(data:any) {
    return http.post<any>('/admin/hospital/updateHospital', data)
}

// 删除医院
export function deleteHospital(id:string) {
    return http.post<any>('/admin/hospital/deleteHospital/'+id)
}

// 获取医院详情
export function getHospital(id:any) {
    return http.get<any>('/admin/hospital/detail/' + id)
}

// 获取医院分页列表
export function getHospitalPage(data:any) {
    return http.get<any>('/admin/hospital/list', data)
}





















