// 请求工具函数
import { hospitalApi, packageApi, appointmentApi, reportApi, userApi } from './api.js';
import service from '../service.js';

/**
 * 统一请求函数
 * @param {Object} options - 请求配置
 * @returns {Promise} 请求结果
 */
export function request(options) {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = service.getToken();
    
    // 构建请求头
    const headers = {
      'Content-Type': 'application/json',
      ...options.header
    };
    
    // 如果有token，添加到请求头
    if (token) {
      headers['Authorization'] = token;
      console.log('请求头已添加Token:', headers);
    } else {
      console.warn('请求未携带Token');
    }
    
    uni.request({
      url: options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: headers,
      success: (res) => {
        console.log('API响应:', res);
        if (res.statusCode === 200) {
          if (res.data && res.data.code === 200) {
            resolve(res.data);
          } else if (res.data && res.data.success === true) {
            resolve(res.data);
          } else {
            reject(new Error(res.data?.message || res.data?.msg || '请求失败'));
          }
        } else if (res.statusCode === 401) {
          // 未授权错误处理
          console.warn('未授权访问，跳转到登录页');
          uni.showToast({
            title: '登录已过期，请重新登录',
            icon: 'none',
            duration: 2000
          });
          // 跳转到登录页
          uni.navigateTo({
            url: '/pages/login/login'
          });
          reject(new Error('请登录后再操作'));
        } else {
          reject(new Error(`HTTP ${res.statusCode}: ${res.data?.message || res.data?.msg || '请求失败'}`));
        }
      },
      fail: (err) => {
        reject(new Error('网络请求失败'));
      }
    });
  });
}

/**
 * GET 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求参数
 * @param {Object} header - 请求头
 * @returns {Promise} 请求结果
 */
export function get(url, data = {}, header = {}) {
  return request({
    url,
    method: 'GET',
    data,
    header
  });
}

/**
 * POST 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求参数
 * @param {Object} header - 请求头
 * @returns {Promise} 请求结果
 */
export function post(url, data = {}, header = {}) {
  return request({
    url,
    method: 'POST',
    data,
    header
  });
}

/**
 * PUT 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求参数
 * @param {Object} header - 请求头
 * @returns {Promise} 请求结果
 */
export function put(url, data = {}, header = {}) {
  return request({
    url,
    method: 'PUT',
    data,
    header
  });
}

/**
 * DELETE 请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求参数
 * @param {Object} header - 请求头
 * @returns {Promise} 请求结果
 */
export function del(url, data = {}, header = {}) {
  return request({
    url,
    method: 'DELETE',
    data,
    header
  });
}

// 导出 API 配置
export { hospitalApi, packageApi, appointmentApi, reportApi, userApi }; 