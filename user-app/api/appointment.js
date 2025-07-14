import { request } from '@/utils/request';
const isDev = process.env.NODE_ENV === 'development';
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取预约列表
 * @returns {Promise} 返回预约列表数据
 */
export function getAppointmentList() {
  return request({
    url: `${API_BASE_URL}/app/appointment/list`,
    method: 'get'
  })
}

/**
 * 获取预约详情
 * @param {String} id 预约ID
 * @returns {Promise} 返回预约详情数据
 */
export function getAppointmentDetail(id) {
  return request({
    url: `${API_BASE_URL}/app/appointment/detail/${id}`,
    method: 'get'
  })
}

/**
 * 取消预约
 * @param {String} id 预约ID
 * @returns {Promise} 返回取消结果
 */
export function cancelAppointment(id) {
  return request({
    url: `${API_BASE_URL}/app/appointment/cancel/${id}`,
    method: 'put'
  })
}

/**
 * 创建预约
 * @param {Object} data 预约数据
 * @returns {Promise} 返回创建结果
 */
export function createAppointment(data) {
  return request({
    url: `${API_BASE_URL}/app/appointment/create`,
    method: 'post',
    data
  })
} 