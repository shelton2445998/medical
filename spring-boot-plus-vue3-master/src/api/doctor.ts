/**
 * 医生端API
 * 所有医生端API都必须以/doctor开头
 */
import {http} from '@/utils/http'
import {LoginParamsType, LoginResult, LoginUserInfoResult} from './types/systemTypes'
import {updatePasswordParamsType, updateUserParamsType} from "@/api/types/userTypes"
import {ListResultType, ResultType} from "@/api/types"
import {UploadParamsTypes} from "@/api/types/uploadTypes"

enum Api {
    // 登录相关
    Login = '/doctor/login',
    Logout = '/doctor/logout',
    GetProfile = '/doctor/info',
    
    // 用户相关
    UpdatePassword = '/doctor/updatePassword',
    UpdateProfile = '/doctor/updateProfile',
    
    // 菜单相关
    GetNavMenuTreeList = '/doctor/sysMenu/getNavMenuTreeList',
    
    // 通用功能
    Upload = '/doctor/common/upload',
    
    // 患者相关
    GetPatientList = '/doctor/patient/list',
    GetPatient = '/doctor/patient/info/',
    AddPatient = '/doctor/patient/add',
    UpdatePatient = '/doctor/patient/update',
    
    // 预约相关
    GetAppointmentList = '/doctor/appointment/list',
    GetAppointment = '/doctor/appointment/info/',
    UpdateAppointment = '/doctor/appointment/update',
    
    // 诊断相关
    GetDiagnosisList = '/doctor/diagnosis/list',
    GetDiagnosis = '/doctor/diagnosis/info/',
    AddDiagnosis = '/doctor/diagnosis/add',
    UpdateDiagnosis = '/doctor/diagnosis/update',
    
    // 处方相关
    GetPrescriptionList = '/doctor/prescription/list',
    GetPrescription = '/doctor/prescription/info/',
    AddPrescription = '/doctor/prescription/add',
    UpdatePrescription = '/doctor/prescription/update',

    // 体检报告相关
    GetReportList = '/doctor/report/list',
    GetReport = '/doctor/report/detail/',
    AddReport = '/doctor/report/add',
    UpdateReport = '/doctor/report/update',
    SaveReportResult = '/doctor/appointment/result',
    ExportReportPdf = '/doctor/report/export/',
    
    // 字典查询
    GetDictList = '/doctor/dict/list',
    
    // 医院科室信息
    GetHospitalList = '/doctor/hospital/list',
    GetDepartmentList = '/doctor/department/list',
}

/**
 * 登录
 */
export function loginApi(data: LoginParamsType) {
    return http.post<LoginResult>(Api.Login, data)
}

/**
 * 获取医生个人信息
 */
export function getProfileApi() {
    return http.post<LoginUserInfoResult>(Api.GetProfile)
}

/**
 * 退出登录
 */
export function logoutApi() {
    return http.post<ResultType>(Api.Logout)
}

/**
 * 修改密码
 */
export function updatePasswordApi(data: updatePasswordParamsType) {
    return http.post<ResultType>(Api.UpdatePassword, data)
}

/**
 * 更新个人资料
 */
export function updateProfileApi(data: updateUserParamsType) {
    return http.post<ResultType>(Api.UpdateProfile, data)
}

/**
 * 获取导航菜单
 */
export function getNavMenuTreeListApi(data: any) {
    return http.post<any>(Api.GetNavMenuTreeList, data)
}

/**
 * 上传文件
 */
export function uploadApi(data: UploadParamsTypes) {
    return http.post<any>(Api.Upload, data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * 获取患者列表
 */
export function getPatientListApi(data: any) {
    return http.post<ListResultType>(Api.GetPatientList, data)
}

/**
 * 获取患者详情
 */
export function getPatientApi(id: string) {
    return http.post<any>(Api.GetPatient + id)
}

/**
 * 添加患者
 */
export function addPatientApi(data: any) {
    return http.post<ResultType>(Api.AddPatient, data)
}

/**
 * 更新患者信息
 */
export function updatePatientApi(data: any) {
    return http.post<ResultType>(Api.UpdatePatient, data)
}

/**
 * 获取预约列表
 */
export function getAppointmentListApi(data: any) {
    return http.post<ListResultType>(Api.GetAppointmentList, data)
}

/**
 * 获取预约详情
 */
export function getAppointmentApi(id: string) {
    return http.post<any>(Api.GetAppointment + id)
}

/**
 * 更新预约信息
 */
export function updateAppointmentApi(data: any) {
    return http.post<ResultType>(Api.UpdateAppointment, data)
}

/**
 * 获取诊断列表
 */
export function getDiagnosisListApi(data: any) {
    return http.post<ListResultType>(Api.GetDiagnosisList, data)
}

/**
 * 获取诊断详情
 */
export function getDiagnosisApi(id: string) {
    return http.post<any>(Api.GetDiagnosis + id)
}

/**
 * 添加诊断
 */
export function addDiagnosisApi(data: any) {
    return http.post<ResultType>(Api.AddDiagnosis, data)
}

/**
 * 更新诊断信息
 */
export function updateDiagnosisApi(data: any) {
    return http.post<ResultType>(Api.UpdateDiagnosis, data)
}

/**
 * 获取处方列表
 */
export function getPrescriptionListApi(data: any) {
    return http.post<ListResultType>(Api.GetPrescriptionList, data)
}

/**
 * 获取处方详情
 */
export function getPrescriptionApi(id: string) {
    return http.post<any>(Api.GetPrescription + id)
}

/**
 * 添加处方
 */
export function addPrescriptionApi(data: any) {
    return http.post<ResultType>(Api.AddPrescription, data)
}

/**
 * 更新处方信息
 */
export function updatePrescriptionApi(data: any) {
    return http.post<ResultType>(Api.UpdatePrescription, data)
}

/**
 * 获取体检报告列表
 */
export function getReportListApi(data: any) {
    return http.post<ListResultType>(Api.GetReportList, data)
}

/**
 * 获取体检报告详情
 */
export function getReportApi(id: string) {
    return http.post<any>(Api.GetReport + id)
}

/**
 * 添加体检报告
 */
export function addReportApi(data: any) {
    return http.post<ResultType>(Api.AddReport, data)
}

/**
 * 更新体检报告
 */
export function updateReportApi(data: any) {
    return http.post<ResultType>(Api.UpdateReport, data)
}

/**
 * 保存体检结果
 */
export function saveReportResultApi(data: any) {
    return http.post<ResultType>(Api.SaveReportResult, data)
}

/**
 * 导出体检报告PDF
 */
export function exportReportPdfApi(id: string) {
    return http.post<Blob>(Api.ExportReportPdf + id, {}, {
        responseType: 'blob'
    })
}

/**
 * 获取字典列表
 */
export function getDictListApi(data: any) {
    return http.post<any>(Api.GetDictList, data)
}

/**
 * 获取医院列表
 */
export function getHospitalListApi() {
    return http.post<any>(Api.GetHospitalList)
}

/**
 * 获取科室列表
 */
export function getDepartmentListApi(hospitalId?: string) {
    return http.post<any>(Api.GetDepartmentList, { hospitalId })
} 