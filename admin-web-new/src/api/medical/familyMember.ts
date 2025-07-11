import {http} from '@/utils/http'

// 添加家庭成员
export function addFamilyMember(data:any) {
    return http.post<any>('/admin/familyMember/addFamilyMember', data)
}

// 修改家庭成员
export function updateFamilyMember(data:any) {
    return http.post<any>('/admin/familyMember/updateFamilyMember', data)
}

// 删除家庭成员
export function deleteFamilyMember(id:string) {
    return http.post<any>('/admin/familyMember/deleteFamilyMember/'+id)
}

// 获取家庭成员详情
export function getFamilyMember(id:any) {
    return http.post<any>('/admin/familyMember/getFamilyMember/'+id)
}

// 获取家庭成员分页列表
export function getFamilyMemberPage(data:any) {
    return http.post<any>('/admin/familyMember/getFamilyMemberPage', data)
}





















