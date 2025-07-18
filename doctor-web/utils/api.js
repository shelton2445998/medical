/**
 * 医生端 API 接口配置文件
 * 
 * 定义了医生端应用所需的所有 API 接口地址
 * 支持开发环境和生产环境的自动切换
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

// 环境判断，根据 NODE_ENV 确定当前运行环境
const isDev = process.env.NODE_ENV === 'development';

// API 基础地址配置
// 开发环境：使用本地服务器地址，便于调试
// 生产环境：使用线上服务器地址
const API_BASE_URL = isDev
    ? 'http://localhost:8888/api'  // 开发环境使用本地地址
    : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 医生端认证相关接口配置
 * 
 * 包含医生登录、退出登录、获取医生信息等认证相关的接口
 */
export const authApi = {
    // 医生登录接口 - 医生账号密码登录
    login: `${API_BASE_URL}/doctor/login`,
    
    // 退出登录接口 - 清除医生登录状态
    logout: `${API_BASE_URL}/doctor/logout`,
    
    // 获取医生信息接口 - 获取当前登录医生的详细信息
    getDoctorInfo: `${API_BASE_URL}/doctor/info`
};

/**
 * 医生端预约管理接口配置
 * 
 * 包含预约列表查询、预约详情查看、体检结果提交等功能
 */
export const appointmentApi = {
    // 获取预约列表接口 - 分页获取医生负责的预约列表
    getAppointmentList: `${API_BASE_URL}/doctor/appointment/list`,
    
    // 获取预约详情接口 - 根据预约ID获取预约的详细信息
    // @param {string|number} id - 预约ID
    getAppointmentDetail: (id) => `${API_BASE_URL}/doctor/appointment/detail/${id}`,
    
    // 提交体检结果接口 - 医生提交体检结果和建议
    submitAppointmentResult: `${API_BASE_URL}/doctor/appointment/result`
};

/**
 * 医生端体检报告接口配置
 * 
 * 包含体检报告的查询、详情查看、更新等功能
 */
export const reportApi = {
    // 获取报告列表接口 - 分页获取体检报告列表
    getReportList: `${API_BASE_URL}/doctor/report/list`,
    
    // 获取报告详情接口 - 根据报告ID获取报告的详细信息
    // @param {string|number} id - 报告ID
    getReportDetail: (id) => `${API_BASE_URL}/doctor/report/detail/${id}`,
    
    // 更新报告接口 - 医生更新体检报告内容
    updateReport: `${API_BASE_URL}/doctor/report/update`
};

/**
 * 医生端其他功能接口配置
 * 
 * 包含排班管理、医生信息更新等其他功能接口
 */
export const otherApi = {
    // 获取医生排班接口 - 获取医生的排班信息
    getSchedule: `${API_BASE_URL}/doctor/schedule`,
    
    // 更新医生信息接口 - 医生更新个人信息
    updateDoctorInfo: `${API_BASE_URL}/doctor/update`
};

/**
 * 默认导出所有 API 配置
 * 
 * 提供统一的接口访问方式，便于其他模块引用
 */
export default {
    authApi,
    appointmentApi,
    reportApi,
    otherApi
};