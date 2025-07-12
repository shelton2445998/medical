import request from './index'

// 医生登录
export function doctorLogin(data) {
  return request({
    url: '/doctor/login',
    method: 'post',
    data
  })
}

// 获取医生信息
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

// 医生退出登录
export function doctorLogout() {
  return request({
    url: '/doctor/logout',
    method: 'post'
  })
}

// 获取医生工作台数据
export function getDoctorDashboard() {
  return request({
    url: '/doctor/dashboard',
    method: 'get'
  })
}

// 获取待诊患者列表
export function getWaitingPatients(params) {
  return request({
    url: '/doctor/appointment/list',
    method: 'get',
    params
  })
}

// 获取医生排班信息
export function getDoctorSchedules(params) {
  return request({
    url: '/doctor/schedule/list',
    method: 'get',
    params
  })
}

// 添加医生排班
export function addDoctorSchedule(data) {
  return request({
    url: '/doctor/schedule/add',
    method: 'post',
    data
  })
}

// 更新医生排班
export function updateDoctorSchedule(data) {
  return request({
    url: '/doctor/schedule/update',
    method: 'post',
    data
  })
}

// 删除医生排班
export function deleteDoctorSchedule(id) {
  return request({
    url: `/doctor/schedule/delete/${id}`,
    method: 'post'
  })
}

// 获取预约列表
export function getAppointmentList(params) {
  return request({
    url: '/doctor/appointment/list',
    method: 'get',
    params
  })
}

// 获取预约详情
export function getAppointmentDetail(id) {
  return request({
    url: `/doctor/appointment/detail/${id}`,
    method: 'get'
  })
}

// 取消预约
export function cancelAppointment(id) {
  return request({
    url: `/doctor/appointment/cancel/${id}`,
    method: 'put'
  })
}

// 更新医生个人信息
export function updateDoctorProfile(data) {
  return request({
    url: '/doctor/profile',
    method: 'post',
    data
  })
}

// 获取医生处方列表
export function getPrescriptionList(params) {
  return request({
    url: '/doctor/prescription/list',
    method: 'get',
    params
  })
}

// 添加医生处方
export function addPrescription(data) {
  return request({
    url: '/doctor/prescription/add',
    method: 'post',
    data
  })
}

// 获取医疗记录列表
export function getMedicalRecordList(params) {
  return request({
    url: '/doctor/medical-record/list',
    method: 'get',
    params
  })
}

// 添加医疗记录
export function addMedicalRecord(data) {
  return request({
    url: '/doctor/medical-record/add',
    method: 'post',
    data
  })
}

// 获取患者列表
export function getPatientList(params) {
  return request({
    url: '/doctor/patient/list',
    method: 'get',
    params
  })
}

// 获取患者详情
export function getPatientDetail(id) {
  return request({
    url: `/doctor/patient/detail/${id}`,
    method: 'get'
  })
}

// 录入体检结果
export function submitExaminationResults(data) {
  return request({
    url: '/doctor/appointment/result',
    method: 'post',
    data
  })
}

// 获取体检报告列表
export function getReportList(params) {
  return request({
    url: '/doctor/report/list',
    method: 'get',
    params
  })
}

// 获取报告详情
export function getReportDetail(id) {
  return request({
    url: `/doctor/report/detail/${id}`,
    method: 'get'
  })
}

// 生成体检报告
export function generateReport(data) {
  return request({
    url: '/doctor/report/generate',
    method: 'post',
    data
  })
}

// 更新体检报告
export function updateReport(data) {
  return request({
    url: '/doctor/report/update',
    method: 'put',
    data
  })
}

// 修改个人密码
export function updatePassword(data) {
  return request({
    url: '/doctor/password',
    method: 'put',
    data
  })
}

// 获取患者历史体检记录
export function getPatientExamHistory(id) {
  return request({
    url: `/doctor/patient/exam-history/${id}`,
    method: 'get'
  })
} 