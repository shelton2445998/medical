// 导入请求工具函数
import { request } from '@/utils/request';
// 判断是否为开发环境
const isDev = process.env.NODE_ENV === 'development';
// 根据环境设置API基础URL
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取预约列表
 * @returns {Promise} 返回预约列表数据
 */
export function getAppointmentList() {
  // 发起GET请求获取预约列表
  return request({
    url: `${API_BASE_URL}/app/appointment/list`, // 请求URL
    method: 'get' // 请求方法
  })
}

/**
 * 获取预约详情
 * @param {String} id 预约ID
 * @returns {Promise} 返回预约详情数据
 */
export function getAppointmentDetail(id) {
  // 发起GET请求获取指定预约的详情
  return request({
    url: `${API_BASE_URL}/app/appointment/detail/${id}`, // 请求URL，包含预约ID
    method: 'get' // 请求方法
  })
}

/**
 * 取消预约
 * @param {String} id 预约ID
 * @returns {Promise} 返回取消结果
 */
export function cancelAppointment(id) {
  // 发起PUT请求取消指定预约
  return request({
    url: `${API_BASE_URL}/app/appointment/cancel/${id}`, // 请求URL，包含预约ID
    method: 'put' // 请求方法
  })
}

/**
 * 创建预约
 * @param {Object} data 预约数据
 * @returns {Promise} 返回创建结果
 */
export function createAppointment(data) {
  // 发起POST请求创建新预约
  return request({
    url: `${API_BASE_URL}/app/appointment/create`, // 请求URL
    method: 'post', // 请求方法
    data // 预约数据
  })
} 