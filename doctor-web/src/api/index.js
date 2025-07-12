import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 与vue.config.js中的代理前缀保持一致
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('doctorToken')
    if (token) {
      // 设置请求头中的Authorization字段
      config.headers.Authorization = token
      
      // 同时也设置Cookie（为了兼容后端可能的双重验证）
      document.cookie = `DOCTOR_TOKEN=${token}; path=/`
      
      console.log('发送请求，携带token:', token, '请求URL:', config.url)
    } else {
      console.warn('未找到token，请求可能会被拒绝:', config.url)
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据响应状态码处理
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5000
      })

      // 401或5001: 未登录或token过期
      if (res.code === 401 || res.code === 5001) {
        // 提示用户重新登录
        ElMessage.confirm('您的登录已过期，请重新登录', '登录提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          localStorage.removeItem('doctorToken')
          localStorage.removeItem('doctorInfo')
          window.location.href = '/login'
        })
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    let message = '请求失败'
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未登录或登录已过期'
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
      message = '请求超时，请稍后重试'
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service 