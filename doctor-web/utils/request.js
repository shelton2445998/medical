/**
 * HTTP 请求工具模块
 * 
 * 提供统一的 HTTP 请求封装，包含请求拦截、响应处理、错误处理等功能
 * 支持 GET、POST、PUT、DELETE 等请求方式
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

// 引入 API 配置模块
import { authApi, appointmentApi, reportApi, otherApi } from './doctorApi.js';

/**
 * 统一请求函数
 * 
 * 对 uni.request 进行封装，提供统一的请求处理、响应处理和错误处理
 * 
 * @param {Object} options - 请求配置对象
 * @param {string} options.url - 请求地址
 * @param {string} [options.method='GET'] - 请求方法
 * @param {Object} [options.data={}] - 请求数据
 * @param {Object} [options.header={}] - 请求头
 * @returns {Promise} 返回 Promise 对象，成功时返回响应数据，失败时返回错误信息
 */
export function request(options) {
    return new Promise((resolve, reject) => {
        // 发起 uni-app 请求
        uni.request({
            url: options.url,
            method: options.method || 'GET',
            data: options.data || {},
            header: {
                // 设置默认请求头为 JSON 格式
                'Content-Type': 'application/json',
                // 合并用户自定义请求头
                ...options.header
            },
            success: (res) => {
                // HTTP 状态码为 200 时才处理业务逻辑
                if (res.statusCode === 200) {
                    // 处理业务成功响应（code === 200）
                    if (res.data && res.data.code === 200) {
                        resolve(res.data);
                    } 
                    // 处理另一种成功响应格式（success === true）
                    else if (res.data && res.data.success === true) {
                        resolve(res.data);
                    } 
                    // 业务逻辑失败，抛出错误
                    else {
                        reject(new Error(res.data?.message || res.data?.msg || '请求失败'));
                    }
                } else {
                    // HTTP 状态码不是 200，抛出 HTTP 错误
                    reject(new Error(`HTTP ${res.statusCode}: ${res.data?.message || res.data?.msg || '请求失败'}`));
                }
            },
            fail: (err) => {
                // 网络请求失败，抛出网络错误
                reject(new Error('网络请求失败'));
            }
        });
    });
}

/**
 * GET 请求封装
 * 
 * 用于获取数据的请求方法
 * 
 * @param {string} url - 请求地址
 * @param {Object} [data={}] - 查询参数
 * @param {Object} [header={}] - 请求头
 * @returns {Promise} 返回 Promise 对象
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
 * POST 请求封装
 * 
 * 用于创建数据的请求方法
 * 
 * @param {string} url - 请求地址
 * @param {Object} [data={}] - 请求体数据
 * @param {Object} [header={}] - 请求头
 * @returns {Promise} 返回 Promise 对象
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
 * PUT 请求封装
 * 
 * 用于更新数据的请求方法
 * 
 * @param {string} url - 请求地址
 * @param {Object} [data={}] - 请求体数据
 * @param {Object} [header={}] - 请求头
 * @returns {Promise} 返回 Promise 对象
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
 * DELETE 请求封装
 * 
 * 用于删除数据的请求方法
 * 
 * @param {string} url - 请求地址
 * @param {Object} [data={}] - 请求体数据
 * @param {Object} [header={}] - 请求头
 * @returns {Promise} 返回 Promise 对象
 */
export function del(url, data = {}, header = {}) {
    return request({
        url,
        method: 'DELETE',
        data,
        header
    });
}

// 导出 API 配置模块，便于其他模块使用
export { authApi, appointmentApi, reportApi, otherApi };