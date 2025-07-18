// 导入请求工具函数
import { request } from '@/utils/request';
// 判断是否为开发环境
const isDev = process.env.NODE_ENV === 'development';
// 根据环境设置API基础URL
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取当前登录用户信息
 * @returns {Promise} 返回用户信息数据
 */
export function getLoginUserInfo() {
  // 发起POST请求获取当前登录用户信息
  return request({
    url: `${API_BASE_URL}/app/getLoginUserInfo`, // 请求URL
    method: 'post' // 请求方法
  })
}

/**
 * 根据用户ID获取用户信息
 * @param {Number|String} userId 用户ID
 * @returns {Promise} 返回用户信息数据
 */
export function getUserInfoById(userId) {
  // 发起POST请求根据用户ID获取用户信息
  return request({
    url: `${API_BASE_URL}/app/getUserInfo`, // 请求URL
    method: 'post', // 请求方法
    data: { userId } // 请求数据，包含用户ID
  })
}