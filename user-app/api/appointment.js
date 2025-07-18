/**
 * @fileoverview 用户端预约管理API模块
 * @description 提供预约相关的所有API接口，包括预约列表、详情、创建和取消功能
 * @author 医疗系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 * 
 * @features
 * - 预约列表查询
 * - 预约详情获取
 * - 预约创建功能
 * - 预约取消功能
 * - 环境自适应配置
 * - 统一错误处理
 * 
 * @dependencies
 * - request: 统一请求工具
 * - process.env: 环境变量
 * 
 * @usage
 * 在页面组件中导入相关函数进行预约管理操作
 * 
 * @security
 * - 环境隔离的API地址配置
 * - 统一的请求认证机制
 * - 参数验证和过滤
 * 
 * @performance
 * - 环境自适应减少配置复杂度
 * - 统一的请求工具提升复用性
 * - 合理的API设计减少请求次数
 * 
 * @maintenance
 * - 模块化的API组织
 * - 清晰的函数命名
 * - 便于后续功能扩展
 * 
 * @api_endpoints
 * - GET /app/appointment/list: 获取预约列表
 * - GET /app/appointment/detail/{id}: 获取预约详情
 * - PUT /app/appointment/cancel/{id}: 取消预约
 * - POST /app/appointment/create: 创建预约
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