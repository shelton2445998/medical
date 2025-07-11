// src/api/doctorApi.js

// 环境配置
const isDev = process.env.NODE_ENV === 'development';
const API_BASE_URL = isDev
    ? 'http://localhost:8888/api'  // 开发环境使用本地地址
    : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

// 医生端认证相关接口
export const authApi = {
    // 医生登录
    login: `${API_BASE_URL}/doctor/login`,
    // 退出登录
    logout: `${API_BASE_URL}/doctor/logout`,
    // 获取医生信息
    getDoctorInfo: `${API_BASE_URL}/doctor/info`
};

// 医生端预约管理接口
export const appointmentApi = {
    // 获取预约列表
    getAppointmentList: `${API_BASE_URL}/doctor/appointment/list`,
    // 获取预约详情
    getAppointmentDetail: (id) => `${API_BASE_URL}/doctor/appointment/detail/${id}`,
    // 提交体检结果
    submitAppointmentResult: `${API_BASE_URL}/doctor/appointment/result`
};

// 医生端体检报告接口
export const reportApi = {
    // 获取报告列表
    getReportList: `${API_BASE_URL}/doctor/report/list`,
    // 获取报告详情
    getReportDetail: (id) => `${API_BASE_URL}/doctor/report/detail/${id}`,
    // 更新报告
    updateReport: `${API_BASE_URL}/doctor/report/update`
};

// 医生端其他接口（如有需要）
export const otherApi = {
    // 获取医生排班
    getSchedule: `${API_BASE_URL}/doctor/schedule`,
    // 更新医生信息
    updateDoctorInfo: `${API_BASE_URL}/doctor/update`
};

export default {
    authApi,
    appointmentApi,
    reportApi,
    otherApi
};