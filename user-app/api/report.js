// 导入请求工具函数
import { request } from '@/utils/request';
// 判断是否为开发环境
const isDev = process.env.NODE_ENV === 'development';
// 根据环境设置API基础URL
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取当前用户的所有体检报告列表
 * @returns {Promise} 返回体检报告列表数据
 */
export function getCurrentUserReports() {
  // 发起GET请求获取当前用户的所有体检报告
  return request({
    url: `${API_BASE_URL}/app/reportItem/getCurrentUserReports`, // 请求URL
    method: 'get' // 请求方法
  })
}

/**
 * 获取用户体检报告分页列表
 * @param {Object} query 查询条件
 * @returns {Promise} 返回体检报告分页数据
 */
export function getCurrentUserReportItemPage(query) {
  // 发起POST请求获取当前用户的体检报告分页列表
  return request({
    url: `${API_BASE_URL}/app/reportItem/getCurrentUserReportItemPage`, // 请求URL
    method: 'post', // 请求方法
    data: query // 查询条件数据
  })
}

/**
 * 获取体检报告详情
 * @param {String} id 报告ID
 * @returns {Promise} 返回报告详情数据
 */
export function getReportDetail(id) {
  // 发起GET请求获取指定体检报告的详情
  return request({
    url: `${API_BASE_URL}/app/reportItem/getReportDetail`, // 请求URL
    method: 'get', // 请求方法
    params: { id } // 查询参数，包含报告ID
  })
}

/**
 * 获取检查项详情
 * @param {String} id 检查项ID
 * @returns {Promise} 返回检查项详情
 */
export function getCheckitemById(id) {
  // 发起POST请求获取指定检查项的详情
  return request({
    url: `${API_BASE_URL}/app/checkitem/getAppCheckitem/${id}`, // 请求URL，包含检查项ID
    method: 'post' // 请求方法
  })
}

/**
 * 获取检查项分页列表
 * @param {Object} query 查询条件
 * @returns {Promise} 返回检查项分页数据
 */
export function getCheckitemPage(query) {
  // 发起POST请求获取检查项分页列表
  return request({
    url: `${API_BASE_URL}/app/checkitem/getAppCheckitemPage`, // 请求URL
    method: 'post', // 请求方法
    data: query // 查询条件数据
  })
}

/**
 * 获取检查项结论详情
 * @param {String} reportItemId 报告检查项ID
 * @returns {Promise} 返回检查项结论
 */
export function getReportItemConclusion(reportItemId) {
  // 发起GET请求获取指定报告检查项的结论
  return request({
    url: `${API_BASE_URL}/app/reportItem/getConclusion/${reportItemId}`, // 请求URL，包含报告检查项ID
    method: 'get' // 请求方法
  })
}

/**
 * 更新检查项结论
 * @param {String} reportItemId 报告检查项ID
 * @param {String} conclusionJson JSON格式的结论
 * @returns {Promise} 返回更新结果
 */
export function updateReportItemConclusion(reportItemId, conclusionJson) {
  // 发起POST请求更新指定报告检查项的结论
  return request({
    url: `${API_BASE_URL}/app/reportItem/updateConclusion`, // 请求URL
    method: 'post', // 请求方法
    data: {
      reportItemId, // 报告检查项ID
      conclusionJson // JSON格式的结论数据
    }
  })
}

/**
 * 获取App体检报告总详情
 * @param {String} id 报告ID
 * @returns {Promise} 返回App体检报告总详情数据
 */
export function getAppReportById(id) {
  // 发起POST请求获取指定App体检报告的总详情
  return request({
    url: `${API_BASE_URL}/app/report/getAppReport/${id}`, // 请求URL，包含报告ID
    method: 'post' // 请求方法
  })
}

/**
 * 获取App体检报告分页
 * @param {Object} query 查询条件
 * @returns {Promise} 返回分页数据
 */
export function getAppReportPage(query) {
  // 发起POST请求获取App体检报告分页列表
  return request({
    url: `${API_BASE_URL}/app/report/getAppReportPage`, // 请求URL
    method: 'post', // 请求方法
    data: query // 查询条件数据
  });
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
  });
}

/**
 * 获取App体检报告检查项信息详情
 * @param {String} id 检查项ID
 * @returns {Promise} 返回检查项详情
 */
export function getAppReportItem(id) {
  // 发起POST请求获取指定App体检报告检查项的详情
  return request({
    url: `${API_BASE_URL}/app/reportItem/getAppReportItem/${id}`, // 请求URL，包含检查项ID
    method: 'post' // 请求方法
  });
}