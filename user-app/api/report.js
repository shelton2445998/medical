/**
 * @fileoverview 用户端体检报告管理API模块
 * @description 提供体检报告相关的所有API接口，包括报告查询、详情获取、检查项管理和结论更新功能
 * @author 医疗系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 * 
 * @features
 * - 体检报告列表查询
 * - 报告详情获取
 * - 检查项管理
 * - 结论更新功能
 * - 分页查询支持
 * - 预约关联查询
 * 
 * @dependencies
 * - request: 统一请求工具
 * - process.env: 环境变量
 * 
 * @usage
 * 在页面组件中导入相关函数进行体检报告管理操作
 * 
 * @security
 * - 用户权限验证
 * - 数据访问控制
 * - 敏感信息保护
 * 
 * @performance
 * - 分页查询优化大数据量处理
 * - 缓存机制减少重复请求
 * - 合理的API设计提升响应速度
 * 
 * @maintenance
 * - 模块化的API组织
 * - 清晰的函数命名
 * - 便于后续功能扩展
 * 
 * @api_endpoints
 * - GET /app/reportItem/getCurrentUserReports: 获取用户报告列表
 * - POST /app/reportItem/getCurrentUserReportItemPage: 获取报告分页
 * - GET /app/reportItem/getReportDetail: 获取报告详情
 * - POST /app/checkitem/getAppCheckitem/{id}: 获取检查项详情
 * - POST /app/checkitem/getAppCheckitemPage: 获取检查项分页
 * - GET /app/reportItem/getConclusion/{id}: 获取检查项结论
 * - POST /app/reportItem/updateConclusion: 更新检查项结论
 * - POST /app/report/getAppReport/{id}: 获取App报告详情
 * - POST /app/report/getAppReportPage: 获取App报告分页
 * - GET /app/appointment/detail/{id}: 获取预约详情
 * - POST /app/reportItem/getAppReportItem/{id}: 获取App报告检查项
 */

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