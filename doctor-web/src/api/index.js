/**
 * HTTP 请求配置文件
 * 
 * 基于 axios 创建统一的 HTTP 请求实例，包含请求/响应拦截器
 * 处理 token 认证、错误提示、状态码处理等功能
 * 
 * @author 医生端项目组
 * @date 2024
 * @version 1.0.0
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'

/**
 * 创建 axios 实例
 * 
 * 配置基础的请求参数，包括 baseURL 和超时时间
 */
const service = axios.create({
  baseURL: '/api', // API 基础路径，与 vue.config.js 中的代理前缀保持一致
  timeout: 10000   // 请求超时时间设置为 10 秒
})

/**
 * 请求拦截器
 * 
 * 在每次请求发送前执行，主要功能：
 * - 从 localStorage 获取 token 并添加到请求头
 * - 设置 Cookie 以支持可能的双重验证
 * - 记录请求日志
 */
service.interceptors.request.use(
  config => {
    // 从本地存储获取医生登录 token
    const token = localStorage.getItem('doctorToken')
    
    if (token) {
      // 设置请求头中的 Authorization 字段用于身份验证
      config.headers.Authorization = token
      
      // 同时也设置 Cookie（为了兼容后端可能的双重验证机制）
      document.cookie = `DOCTOR_TOKEN=${token}; path=/`
      
      console.log('发送请求，携带token:', token, '请求URL:', config.url)
    } else {
      console.warn('未找到token，请求可能会被拒绝:', config.url)
    }
    
    return config
  },
  error => {
    // 请求配置错误处理
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 
 * 在每次响应返回后执行，主要功能：
 * - 统一处理业务状态码
 * - 处理 token 过期和权限问题
 * - 统一错误提示
 * - 处理 HTTP 状态码错误
 */
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据业务状态码处理响应
    if (res.code !== 200) {
      // 显示错误提示消息
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5000
      })

      // 处理未登录或 token 过期的情况（401或5001）
      if (res.code === 401 || res.code === 5001) {
        // 提示用户重新登录
        ElMessage.confirm('您的登录已过期，请重新登录', '登录提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 清除本地存储的用户信息
          localStorage.removeItem('doctorToken')
          localStorage.removeItem('doctorInfo')
          // 跳转到登录页面
          window.location.href = '/login'
        })
      }
      
      // 返回错误 Promise
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      // 请求成功，返回响应数据
      return res
    }
  },
  error => {
    // HTTP 状态码错误处理
    console.error('响应错误:', error)
    
    let message = '请求失败'
    
    if (error.response) {
      // 根据 HTTP 状态码提供具体的错误信息
      switch (error.response.status) {
        case 401:
          message = '未登录或登录已过期'
          // 清除本地存储并跳转到登录页
          localStorage.removeItem('doctorToken')
          localStorage.removeItem('doctorInfo')
          window.location.href = '/login'
          break
        case 403:
          message = '没有权限访问该资源'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = `请求失败: ${error.response.status}`
      }
    } else if (error.message.includes('timeout')) {
      // 处理请求超时
      message = '请求超时，请稍后重试'
    }
    
    // 显示错误提示
    ElMessage.error(message)
    
    return Promise.reject(error)
  }
)

// 导出配置好的 axios 实例
export default service 