// 请求工具函数
// 导入各种API接口配置
import { hospitalApi, packageApi, appointmentApi, reportApi, userApi } from './api.js';
// 导入服务层模块
import service from '../service.js';

/**
 * 统一请求函数
 * @param {Object} options - 请求配置
 * @returns {Promise} 请求结果
 */
export function request(options) {
  // 返回一个Promise对象
  return new Promise((resolve, reject) => {
    // 获取用户登录token
    const token = service.getToken();
    
    // 构建请求头对象
    const headers = {
      'Content-Type': 'application/json', // 设置内容类型为JSON
      ...options.header // 展开传入的自定义请求头
    };
    
    // 如果存在token，则添加到请求头中
    if (token) {
      headers['Authorization'] = token; // 设置授权头
      console.log('请求头已添加Token:', headers); // 输出日志
    } else {
      console.warn('请求未携带Token'); // 输出警告日志
    }
    
    // 发起uni-app网络请求
    uni.request({
      url: options.url, // 请求URL
      method: options.method || 'GET', // 请求方法，默认为GET
      data: options.data || {}, // 请求数据，默认为空对象
      header: headers, // 请求头
      success: (res) => {
        // 请求成功回调
        console.log('API响应:', res); // 输出响应日志
        if (res.statusCode === 200) {
          // HTTP状态码为200时的处理
          if (res.data && res.data.code === 200) {
            // 业务状态码为200，请求成功
            resolve(res.data);
          } else if (res.data && res.data.success === true) {
            // 业务成功标志为true，请求成功
            resolve(res.data);
          } else {
            // 业务请求失败，返回错误信息
            reject(new Error(res.data?.message || res.data?.msg || '请求失败'));
          }
        } else if (res.statusCode === 401) {
          // HTTP状态码为401，未授权错误处理
          console.warn('未授权访问，跳转到登录页'); // 输出警告日志
          // 显示提示信息
          uni.showToast({
            title: '登录已过期，请重新登录', // 提示标题
            icon: 'none', // 不显示图标
            duration: 2000 // 显示时长2秒
          });
          // 跳转到登录页面
          uni.navigateTo({
            url: '/pages/login/login' // 登录页面路径
          });
          // 拒绝Promise并返回错误信息
          reject(new Error('请登录后再操作'));
        } else {
          // 其他HTTP错误状态码处理
          reject(new Error(`HTTP ${res.statusCode}: ${res.data?.message || res.data?.msg || '请求失败'}`));
        }
      },
      fail: (err) => {
        // 请求失败回调
        reject(new Error('网络请求失败')); // 拒绝Promise并返回错误信息
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
  // 调用统一请求函数，设置方法为GET
  return request({
    url, // 请求URL
    method: 'GET', // 请求方法
    data, // 请求参数
    header // 请求头
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
  // 调用统一请求函数，设置方法为POST
  return request({
    url, // 请求URL
    method: 'POST', // 请求方法
    data, // 请求参数
    header // 请求头
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
  // 调用统一请求函数，设置方法为PUT
  return request({
    url, // 请求URL
    method: 'PUT', // 请求方法
    data, // 请求参数
    header // 请求头
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
  // 调用统一请求函数，设置方法为DELETE
  return request({
    url, // 请求URL
    method: 'DELETE', // 请求方法
    data, // 请求参数
    header // 请求头
  });
}

// 导出 API 配置供其他模块使用
export { hospitalApi, packageApi, appointmentApi, reportApi, userApi }; 