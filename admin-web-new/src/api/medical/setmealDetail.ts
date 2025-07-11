import {http} from '@/utils/http'

// 体检套餐明细接口类型
export interface SetmealDetail {
    id?: number | null;
    setmealId: number;
    name?: string;
    price?: number;
    discountPrice?: number;
    description?: string;
    checkItems?: string;
    suitableCrowd?: string;
    appointmentNotice?: string;
    userReviews?: string;
    hospitalId?: number;
    departmentId?: number;
    packageType?: number;
    status?: number;
    sortOrder?: number;
    itemId: number;
    createId?: number;
    createTime?: string;
    updateId?: number;
    updateTime?: string;
}

// 添加体检套餐明细
export function addSetmealDetail(data:SetmealDetail) {
    return http.post<any>('/admin/setmealDetail/addSetmealDetail', data)
}

// 修改体检套餐明细
export function updateSetmealDetail(data:SetmealDetail) {
    return http.post<any>('/admin/setmealDetail/updateSetmealDetail', data)
}

// 删除体检套餐明细
export function deleteSetmealDetail(id:string) {
    return http.post<any>('/admin/setmealDetail/deleteSetmealDetail/'+id)
}

// 获取体检套餐明细详情
export function getSetmealDetail(id:any) {
    return http.post<any>('/admin/setmealDetail/getSetmealDetail/'+id)
}

// 获取体检套餐明细分页列表
export function getSetmealDetailPage(data:any) {
    return http.post<any>('/admin/setmealDetail/getSetmealDetailPage', data)
}





















