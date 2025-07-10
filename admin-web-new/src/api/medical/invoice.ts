import {http} from '@/utils/http'

// 添加电子发票
export function addInvoice(data:any) {
    return http.post<any>('/admin/invoice/addInvoice', data)
}

// 修改电子发票
export function updateInvoice(data:any) {
    return http.post<any>('/admin/invoice/updateInvoice', data)
}

// 删除电子发票
export function deleteInvoice(id:string) {
    return http.post<any>('/admin/invoice/deleteInvoice/'+id)
}

// 获取电子发票详情
export function getInvoice(id:any) {
    return http.post<any>('/admin/invoice/getInvoice/'+id)
}

// 获取电子发票分页列表
export function getInvoicePage(data:any) {
    return http.post<any>('/admin/invoice/getInvoicePage', data)
}





















