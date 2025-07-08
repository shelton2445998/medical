import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api', // API 基础URL
  timeout: 15000 // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('admin-token')
    if (token) {
      // 设置请求头 Authorization
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据约定的状态码判断请求是否成功
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      // 401: Token 过期或未登录
      if (res.code === 401) {
        // 清除 token 并跳转到登录页
        localStorage.removeItem('admin-token')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    let message = error.message
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          // 清除 token 并跳转到登录页
          localStorage.removeItem('admin-token')
          window.location.href = '/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址错误'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = error.response.data.message || '请求失败'
      }
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// API 接口

// 登录接口
export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 退出登录
export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

// 获取管理员信息
export function getAdminInfo() {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

// 用户管理接口
export const userApi = {
  // 获取用户列表
  getList(params) {
    return request({
      url: '/admin/user/list',
      method: 'get',
      params
    })
  },
  // 添加用户
  add(data) {
    return request({
      url: '/admin/user/add',
      method: 'post',
      data
    })
  },
  // 更新用户
  update(data) {
    return request({
      url: '/admin/user/update',
      method: 'put',
      data
    })
  },
  // 获取用户详情
  getDetail(userId) {
    return request({
      url: `/admin/user/detail/${userId}`,
      method: 'get'
    })
  },
  // 更新用户状态
  updateStatus(userId, status) {
    return request({
      url: '/admin/user/status',
      method: 'put',
      data: {
        userId,
        status
      }
    })
  },
  // 重置用户密码
  resetPassword(data) {
    return request({
      url: '/admin/user/reset-password',
      method: 'put',
      data
    })
  }
}

// 医生管理接口
export const doctorApi = {
  // 获取医生列表
  getList(params) {
    return request({
      url: '/admin/doctor/list',
      method: 'get',
      params
    })
  },
  // 添加医生
  add(data) {
    return request({
      url: '/admin/doctor/add',
      method: 'post',
      data
    })
  },
  // 更新医生
  update(data) {
    return request({
      url: '/admin/doctor/update',
      method: 'put',
      data
    })
  },
  // 获取医生详情
  getDetail(doctorId) {
    return request({
      url: `/admin/doctor/detail/${doctorId}`,
      method: 'get'
    })
  },
  // 更新医生状态
  updateStatus(doctorId, status) {
    return request({
      url: '/admin/doctor/status',
      method: 'put',
      data: {
        doctorId,
        status
      }
    })
  },
  // 重置医生密码
  resetPassword(data) {
    return request({
      url: '/admin/doctor/reset-password',
      method: 'put',
      data
    })
  }
}

// 体检套餐管理接口
export const packageApi = {
  // 获取套餐列表
  getList(params) {
    return request({
      url: '/admin/package/list',
      method: 'get',
      params
    })
  },
  // 添加套餐
  add(data) {
    return request({
      url: '/admin/package/add',
      method: 'post',
      data
    })
  },
  // 更新套餐
  update(data) {
    return request({
      url: '/admin/package/update',
      method: 'put',
      data
    })
  },
  // 获取套餐详情
  getDetail(packageId) {
    return request({
      url: `/admin/package/detail/${packageId}`,
      method: 'get'
    })
  },
  // 更新套餐状态
  updateStatus(packageId, status) {
    return request({
      url: '/admin/package/status',
      method: 'put',
      data: {
        packageId,
        status
      }
    })
  },
  // 删除套餐
  delete(packageId) {
    return request({
      url: `/admin/package/delete/${packageId}`,
      method: 'delete'
    })
  }
}

// 体检项目管理接口
export const checkItemApi = {
  // 获取项目列表
  getList(params) {
    return request({
      url: '/admin/item/list',
      method: 'get',
      params
    })
  },
  // 添加项目
  add(data) {
    return request({
      url: '/admin/item/add',
      method: 'post',
      data
    })
  },
  // 更新项目
  update(data) {
    return request({
      url: '/admin/item/update',
      method: 'put',
      data
    })
  },
  // 获取项目详情
  getDetail(itemId) {
    return request({
      url: `/admin/item/detail/${itemId}`,
      method: 'get'
    })
  },
  // 更新项目状态
  updateStatus(itemId, status) {
    return request({
      url: '/admin/item/status',
      method: 'put',
      data: {
        itemId,
        status
      }
    })
  },
  // 删除项目
  delete(itemId) {
    return request({
      url: `/admin/item/delete/${itemId}`,
      method: 'delete'
    })
  }
}

// 医院管理接口
export const hospitalApi = {
  // 获取医院列表
  getList(params) {
    return request({
      url: '/admin/hospital/list',
      method: 'get',
      params
    })
  },
  // 添加医院
  add(data) {
    return request({
      url: '/admin/hospital/add',
      method: 'post',
      data
    })
  },
  // 更新医院
  update(data) {
    return request({
      url: '/admin/hospital/update',
      method: 'put',
      data
    })
  },
  // 获取医院详情
  getDetail(hospitalId) {
    return request({
      url: `/admin/hospital/detail/${hospitalId}`,
      method: 'get'
    })
  },
  // 更新医院状态
  updateStatus(hospitalId, status) {
    return request({
      url: '/admin/hospital/status',
      method: 'put',
      data: {
        hospitalId,
        status
      }
    })
  }
}

// 预约管理接口
export const appointmentApi = {
  // 获取预约列表
  getList(params) {
    return request({
      url: '/admin/appointment/list',
      method: 'get',
      params
    })
  },
  // 获取预约详情
  getDetail(appointmentId) {
    return request({
      url: `/admin/appointment/detail/${appointmentId}`,
      method: 'get'
    })
  },
  // 更新预约状态
  updateStatus(data) {
    return request({
      url: '/admin/appointment/status',
      method: 'put',
      data
    })
  },
  // 统计数据
  getStatistics(params) {
    return request({
      url: '/admin/appointment/statistics',
      method: 'get',
      params
    })
  }
}

// 系统设置接口
export const settingApi = {
  // 获取系统配置
  getSettings() {
    return request({
      url: '/admin/settings',
      method: 'get'
    })
  },
  // 更新系统配置
  updateSettings(data) {
    return request({
      url: '/admin/settings',
      method: 'put',
      data
    })
  }
}

export default request 