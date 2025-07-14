import { request } from '@/utils/request';
const isDev = process.env.NODE_ENV === 'development';
const API_BASE_URL = isDev 
  ? 'http://localhost:8888/api'  // 开发环境使用本地地址
  : 'http://39.104.57.236:8888/api';  // 生产环境使用服务器地址

/**
 * 获取当前登录用户信息
 * @returns {Promise} 返回用户信息数据
 */
export function getLoginUserInfo() {
  return request({
    url: `${API_BASE_URL}/app/getLoginUserInfo`,
    method: 'post'
  })
}

/**
 * 根据用户ID获取用户信息
 * @param {Number|String} userId 用户ID
 * @returns {Promise} 返回用户信息数据
 */
export function getUserInfoById(userId) {
  return request({
    url: `${API_BASE_URL}/app/getUserInfo`,
    method: 'post',
    data: { userId }
  })
}