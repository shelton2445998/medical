/**
 * 医生端业务 API 接口模块
 * 
 * 定义了医生端应用的所有业务接口函数
 * 包含登录认证、工作台、排班管理、预约管理、处方管理、医疗记录、患者管理、体检报告等功能
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

import request from './index'

// ==================== 认证相关接口 ====================

/**
 * 医生登录接口
 * 
 * 用于医生账号密码登录验证
 * 
 * @param {Object} data - 登录参数
 * @param {string} data.username - 医生用户名
 * @param {string} data.password - 医生密码
 * @returns {Promise} 返回登录结果，包含 token 和用户信息
 */
export function doctorLogin(data) {
  return request({
    url: '/doctor/login',
    method: 'post',
    data
  })
}

/**
 * 获取医生信息接口
 * 
 * 获取当前登录医生的详细信息
 * 包含调试日志记录，便于排查问题
 * 
 * @returns {Promise} 返回医生详细信息
 */
export function getDoctorInfo() {
  console.log('正在请求医生信息API')
  return request({
    url: '/doctor/info',
    method: 'get'
  }).then(response => {
    console.log('医生信息API响应:', response)
    return response
  }).catch(error => {
    console.error('医生信息API错误:', error)
    throw error
  })
}

/**
 * 医生退出登录接口
 * 
 * 清除服务器端的登录状态
 * 
 * @returns {Promise} 返回退出登录结果
 */
export function doctorLogout() {
  return request({
    url: '/doctor/logout',
    method: 'post'
  })
}

// ==================== 工作台相关接口 ====================

/**
 * 获取医生工作台数据接口
 * 
 * 获取医生工作台的概览数据，包括统计信息、待处理事项等
 * 
 * @returns {Promise} 返回工作台数据
 */
export function getDoctorDashboard() {
  return request({
    url: '/doctor/dashboard',
    method: 'get'
  })
}

/**
 * 获取待诊患者列表接口
 * 
 * 获取医生需要处理的待诊患者列表
 * 
 * @param {Object} params - 查询参数
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回待诊患者列表
 */
export function getWaitingPatients(params) {
  return request({
    url: '/doctor/appointment/list',
    method: 'get',
    params
  })
}

// ==================== 排班管理相关接口 ====================

/**
 * 获取医生排班信息接口
 * 
 * 获取医生的排班信息列表
 * 
 * @param {Object} params - 查询参数
 * @param {string} [params.date] - 查询日期
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回排班信息列表
 */
export function getDoctorSchedules(params) {
  return request({
    url: '/doctor/schedule/list',
    method: 'get',
    params
  })
}

/**
 * 添加医生排班接口
 * 
 * 创建新的医生排班记录
 * 
 * @param {Object} data - 排班数据
 * @param {string} data.date - 排班日期
 * @param {string} data.timeSlot - 时间段
 * @param {number} data.maxPatients - 最大患者数
 * @returns {Promise} 返回创建结果
 */
export function addDoctorSchedule(data) {
  return request({
    url: '/doctor/schedule/add',
    method: 'post',
    data
  })
}

/**
 * 更新医生排班接口
 * 
 * 更新现有的医生排班记录
 * 
 * @param {Object} data - 排班数据
 * @param {number} data.id - 排班ID
 * @param {string} [data.date] - 排班日期
 * @param {string} [data.timeSlot] - 时间段
 * @param {number} [data.maxPatients] - 最大患者数
 * @returns {Promise} 返回更新结果
 */
export function updateDoctorSchedule(data) {
  return request({
    url: '/doctor/schedule/update',
    method: 'post',
    data
  })
}

/**
 * 删除医生排班接口
 * 
 * 删除指定的医生排班记录
 * 
 * @param {number} id - 排班ID
 * @returns {Promise} 返回删除结果
 */
export function deleteDoctorSchedule(id) {
  return request({
    url: `/doctor/schedule/delete/${id}`,
    method: 'post'
  })
}

// ==================== 预约管理相关接口 ====================

/**
 * 获取预约列表接口
 * 
 * 获取医生的预约管理列表
 * 
 * @param {Object} params - 查询参数
 * @param {string} [params.status] - 预约状态
 * @param {string} [params.date] - 预约日期
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回预约列表
 */
export function getAppointmentList(params) {
  return request({
    url: '/doctor/appointment/list',
    method: 'get',
    params
  })
}

/**
 * 获取预约详情接口
 * 
 * 获取指定预约的详细信息
 * 
 * @param {number} id - 预约ID
 * @returns {Promise} 返回预约详情
 */
export function getAppointmentDetail(id) {
  return request({
    url: `/doctor/appointment/detail/${id}`,
    method: 'get'
  })
}

/**
 * 取消预约接口
 * 
 * 医生取消指定的预约
 * 
 * @param {number} id - 预约ID
 * @returns {Promise} 返回取消结果
 */
export function cancelAppointment(id) {
  return request({
    url: `/doctor/appointment/cancel/${id}`,
    method: 'put'
  })
}

// ==================== 个人信息管理相关接口 ====================

/**
 * 更新医生个人信息接口
 * 
 * 更新医生的个人资料信息
 * 
 * @param {Object} data - 个人信息数据
 * @param {string} [data.name] - 姓名
 * @param {string} [data.phone] - 电话
 * @param {string} [data.email] - 邮箱
 * @param {string} [data.specialty] - 专业
 * @returns {Promise} 返回更新结果
 */
export function updateDoctorProfile(data) {
  return request({
    url: '/doctor/profile',
    method: 'post',
    data
  })
}

// ==================== 处方管理相关接口 ====================

/**
 * 获取医生处方列表接口
 * 
 * 获取医生开具的处方列表
 * 
 * @param {Object} params - 查询参数
 * @param {string} [params.patientName] - 患者姓名
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回处方列表
 */
export function getPrescriptionList(params) {
  return request({
    url: '/doctor/prescription/list',
    method: 'get',
    params
  })
}

/**
 * 添加医生处方接口
 * 
 * 医生为患者开具新的处方
 * 
 * @param {Object} data - 处方数据
 * @param {number} data.patientId - 患者ID
 * @param {Array} data.medications - 药物列表
 * @param {string} data.diagnosis - 诊断信息
 * @param {string} [data.notes] - 备注信息
 * @returns {Promise} 返回创建结果
 */
export function addPrescription(data) {
  return request({
    url: '/doctor/prescription/add',
    method: 'post',
    data
  })
}

// ==================== 医疗记录管理相关接口 ====================

/**
 * 获取医疗记录列表接口
 * 
 * 获取医生创建的医疗记录列表
 * 
 * @param {Object} params - 查询参数
 * @param {number} [params.patientId] - 患者ID
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回医疗记录列表
 */
export function getMedicalRecordList(params) {
  return request({
    url: '/doctor/medical-record/list',
    method: 'get',
    params
  })
}

/**
 * 添加医疗记录接口
 * 
 * 创建新的医疗记录
 * 
 * @param {Object} data - 医疗记录数据
 * @param {number} data.patientId - 患者ID
 * @param {string} data.symptoms - 症状描述
 * @param {string} data.diagnosis - 诊断结果
 * @param {string} data.treatment - 治疗方案
 * @param {string} [data.notes] - 备注信息
 * @returns {Promise} 返回创建结果
 */
export function addMedicalRecord(data) {
  return request({
    url: '/doctor/medical-record/add',
    method: 'post',
    data
  })
}

// ==================== 患者管理相关接口 ====================

/**
 * 获取患者列表接口
 * 
 * 获取医生管理的患者列表
 * 
 * @param {Object} params - 查询参数
 * @param {string} [params.name] - 患者姓名
 * @param {string} [params.phone] - 患者电话
 * @param {string} [params.status] - 患者状态
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回患者列表
 */
export function getPatientList(params) {
  return request({
    url: '/doctor/patient/list',
    method: 'get',
    params
  })
}

/**
 * 获取患者详情接口
 * 
 * 获取指定患者的详细信息
 * 
 * @param {number} id - 患者ID
 * @returns {Promise} 返回患者详情
 */
export function getPatientDetail(id) {
  return request({
    url: `/doctor/patient/detail/${id}`,
    method: 'get'
  })
}

/**
 * 获取患者历史体检记录接口
 * 
 * 获取指定患者的历史体检记录
 * 
 * @param {number} id - 患者ID
 * @returns {Promise} 返回患者历史体检记录
 */
export function getPatientExamHistory(id) {
  return request({
    url: `/doctor/patient/exam-history/${id}`,
    method: 'get'
  })
}

// ==================== 体检报告管理相关接口 ====================

/**
 * 录入体检结果接口
 * 
 * 医生录入或更新体检结果
 * 
 * @param {Object} data - 体检结果数据
 * @param {number} data.reportId - 报告ID
 * @param {Object} data.results - 体检结果
 * @param {string} [data.conclusion] - 检查结论
 * @param {string} [data.suggestions] - 建议
 * @returns {Promise} 返回录入结果
 */
export function submitExaminationResults(data) {
  return request({
    url: '/doctor/report/update',
    method: 'post',
    data
  })
}

/**
 * 获取体检报告列表接口
 * 
 * 获取医生负责的体检报告列表
 * 
 * @param {Object} params - 查询参数
 * @param {string} [params.status] - 报告状态
 * @param {string} [params.patientName] - 患者姓名
 * @param {string} [params.startDate] - 开始日期
 * @param {string} [params.endDate] - 结束日期
 * @param {number} [params.page=1] - 页码
 * @param {number} [params.size=10] - 每页数量
 * @returns {Promise} 返回体检报告列表
 */
export function getReportList(params) {
  return request({
    url: '/doctor/report/list',
    method: 'get',
    params
  })
}

/**
 * 获取待处理报告列表接口
 * 
 * 获取需要医生处理的待处理体检报告
 * 
 * @returns {Promise} 返回待处理报告列表
 */
export function getPendingReports() {
  return request({
    url: '/doctor/report/pending',
    method: 'get'
  })
}

/**
 * 获取已完成报告列表接口
 * 
 * 获取医生已完成的体检报告列表
 * 
 * @returns {Promise} 返回已完成报告列表
 */
export function getCompletedReports() {
  return request({
    url: '/doctor/report/completed',
    method: 'get'
  })
}

/**
 * 获取报告详情接口
 * 
 * 获取指定体检报告的详细信息
 * 
 * @param {number} id - 报告ID
 * @returns {Promise} 返回报告详情
 */
export function getReportDetail(id) {
  return request({
    url: `/doctor/report/detail/${id}`,
    method: 'get'
  })
}

/**
 * 生成体检报告接口
 * 
 * 根据体检结果生成正式的体检报告
 * 
 * @param {Object} data - 报告生成数据
 * @param {number} data.appointmentId - 预约ID
 * @param {Object} data.results - 体检结果
 * @param {string} data.conclusion - 检查结论
 * @param {string} [data.suggestions] - 建议
 * @returns {Promise} 返回生成结果
 */
export function generateReport(data) {
  return request({
    url: '/doctor/report/generate',
    method: 'post',
    data
  })
}

/**
 * 更新体检报告接口
 * 
 * 更新现有的体检报告内容
 * 
 * @param {Object} data - 报告更新数据
 * @param {number} data.reportId - 报告ID
 * @param {Object} [data.results] - 体检结果
 * @param {string} [data.conclusion] - 检查结论
 * @param {string} [data.suggestions] - 建议
 * @returns {Promise} 返回更新结果
 */
export function updateReport(data) {
  return request({
    url: '/doctor/report/update',
    method: 'put',
    data
  })
}

// ==================== 安全管理相关接口 ====================

/**
 * 修改医生密码
 * 
 * @param {Object} data - 密码修改数据
 * @param {string} data.oldPassword - 原密码
 * @param {string} data.newPassword - 新密码
 * @returns {Promise} 返回修改结果
 */
export function updatePassword(data) {
  return request({
    url: '/doctor/password',
    method: 'put',
    data
  })
} 