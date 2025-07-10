import {http} from '@/utils/http'

// 添加科室
export function addDepartment(data:any) {
    return http.post<any>('/admin/department/addDepartment', data)
}

// 修改科室
export function updateDepartment(data:any) {
    return http.post<any>('/admin/department/updateDepartment', data)
}

// 删除科室
export function deleteDepartment(id:string) {
    return http.post<any>('/admin/department/deleteDepartment/'+id)
}

// 获取科室详情
export function getDepartment(id:any) {
    return http.post<any>('/admin/department/getDepartment/'+id)
}

// 获取科室分页列表
export function getDepartmentPage(data:any) {
    return http.post<any>('/admin/department/getDepartmentPage', data)
}





















