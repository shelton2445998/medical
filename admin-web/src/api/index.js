import axios from 'axios'
import { ElMessage } from 'element-plus'
import * as mockData from './mock'

// 是否使用模拟数据
const useMockData = true;

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

// 创建一个包装函数，根据是否使用模拟数据返回不同的结果
const apiWrapper = (apiFunc, mockData) => {
  return (...args) => {
    if (useMockData) {
      console.log('使用模拟数据');
      return mockData.mockResponse(mockData, 500);
    } else {
      return apiFunc(...args);
    }
  }
};

// API 接口

// 登录接口
export function login(data) {
  if (useMockData) {
    console.log('使用模拟登录');
    // 模拟登录，任何账号密码都可以
    return mockData.mockResponse({ 
      token: 'mock-admin-token-' + Date.now(),
      userInfo: mockData.adminInfo
    });
  }
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 退出登录
export function logout() {
  if (useMockData) {
    return mockData.mockResponse({ success: true });
  }
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

// 获取管理员信息
export function getAdminInfo() {
  if (useMockData) {
    return mockData.mockResponse(mockData.adminInfo);
  }
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

// 获取仪表盘数据
export function getDashboardData() {
  if (useMockData) {
    return mockData.mockResponse(mockData.dashboardData);
  }
  return request({
    url: '/admin/dashboard',
    method: 'get'
  })
}

// 用户管理接口
export const userApi = {
  // 获取用户列表
  getList(params) {
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.userData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/user/list',
      method: 'get',
      params
    })
  },
  // 添加用户
  add(data) {
    if (useMockData) {
      const newUser = {
        ...data,
        userId: mockData.userData.list.length + 1,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString()
      };
      mockData.userData.list.unshift(newUser);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/user/add',
      method: 'post',
      data
    })
  },
  // 更新用户
  update(data) {
    if (useMockData) {
      const index = mockData.userData.list.findIndex(item => item.userId === data.userId);
      if (index !== -1) {
        mockData.userData.list[index] = {
          ...mockData.userData.list[index],
          ...data,
          updateTime: new Date().toLocaleString()
        };
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/user/update',
      method: 'put',
      data
    })
  },
  // 获取用户详情
  getDetail(userId) {
    if (useMockData) {
      const user = mockData.userData.list.find(item => item.userId == userId);
      return mockData.mockResponse(user || {});
    }
    return request({
      url: `/admin/user/detail/${userId}`,
      method: 'get'
    })
  },
  // 更新用户状态
  updateStatus(userId, status) {
    if (useMockData) {
      const index = mockData.userData.list.findIndex(item => item.userId == userId);
      if (index !== -1) {
        mockData.userData.list[index].status = status;
        mockData.userData.list[index].updateTime = new Date().toLocaleString();
      }
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.doctorData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/doctor/list',
      method: 'get',
      params
    })
  },
  // 添加医生
  add(data) {
    if (useMockData) {
      const newDoctor = {
        ...data,
        doctorId: mockData.doctorData.list.length + 1,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString()
      };
      mockData.doctorData.list.unshift(newDoctor);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/doctor/add',
      method: 'post',
      data
    })
  },
  // 更新医生
  update(data) {
    if (useMockData) {
      const index = mockData.doctorData.list.findIndex(item => item.doctorId === data.doctorId);
      if (index !== -1) {
        mockData.doctorData.list[index] = {
          ...mockData.doctorData.list[index],
          ...data,
          updateTime: new Date().toLocaleString()
        };
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/doctor/update',
      method: 'put',
      data
    })
  },
  // 获取医生详情
  getDetail(doctorId) {
    if (useMockData) {
      const doctor = mockData.doctorData.list.find(item => item.doctorId == doctorId);
      return mockData.mockResponse(doctor || {});
    }
    return request({
      url: `/admin/doctor/detail/${doctorId}`,
      method: 'get'
    })
  },
  // 更新医生状态
  updateStatus(doctorId, status) {
    if (useMockData) {
      const index = mockData.doctorData.list.findIndex(item => item.doctorId == doctorId);
      if (index !== -1) {
        mockData.doctorData.list[index].status = status;
        mockData.doctorData.list[index].updateTime = new Date().toLocaleString();
      }
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.packageData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/package/list',
      method: 'get',
      params
    })
  },
  // 添加套餐
  add(data) {
    if (useMockData) {
      const newPackage = {
        ...data,
        id: mockData.packageData.list.length + 1,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString(),
        salesCount: 0
      };
      mockData.packageData.list.unshift(newPackage);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/package/add',
      method: 'post',
      data
    })
  },
  // 更新套餐
  update(data) {
    if (useMockData) {
      const index = mockData.packageData.list.findIndex(item => item.id === data.id);
      if (index !== -1) {
        mockData.packageData.list[index] = {
          ...mockData.packageData.list[index],
          ...data,
          updateTime: new Date().toLocaleString()
        };
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/package/update',
      method: 'put',
      data
    })
  },
  // 获取套餐详情
  getDetail(packageId) {
    if (useMockData) {
      const pkg = mockData.packageData.list.find(item => item.id == packageId);
      return mockData.mockResponse(pkg || {});
    }
    return request({
      url: `/admin/package/detail/${packageId}`,
      method: 'get'
    })
  },
  // 更新套餐状态
  updateStatus(packageId, status) {
    if (useMockData) {
      const index = mockData.packageData.list.findIndex(item => item.id == packageId);
      if (index !== -1) {
        mockData.packageData.list[index].status = status;
        mockData.packageData.list[index].updateTime = new Date().toLocaleString();
      }
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      mockData.packageData.list = mockData.packageData.list.filter(item => item.id != packageId);
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.checkItemData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/check-item/list',
      method: 'get',
      params
    })
  },
  // 添加项目
  add(data) {
    if (useMockData) {
      const newItem = {
        ...data,
        id: mockData.checkItemData.list.length + 1
      };
      mockData.checkItemData.list.unshift(newItem);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/check-item/add',
      method: 'post',
      data
    })
  },
  // 更新项目
  update(data) {
    if (useMockData) {
      const index = mockData.checkItemData.list.findIndex(item => item.id === data.id);
      if (index !== -1) {
        mockData.checkItemData.list[index] = {
          ...mockData.checkItemData.list[index],
          ...data
        };
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/check-item/update',
      method: 'put',
      data
    })
  },
  // 获取项目详情
  getDetail(itemId) {
    if (useMockData) {
      const item = mockData.checkItemData.list.find(item => item.id == itemId);
      return mockData.mockResponse(item || {});
    }
    return request({
      url: `/admin/check-item/detail/${itemId}`,
      method: 'get'
    })
  },
  // 更新项目状态
  updateStatus(itemId, status) {
    if (useMockData) {
      const index = mockData.checkItemData.list.findIndex(item => item.id == itemId);
      if (index !== -1) {
        mockData.checkItemData.list[index].status = status;
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/check-item/status',
      method: 'put',
      data: {
        itemId,
        status
      }
    })
  },
  // 删除项目
  delete(itemId) {
    if (useMockData) {
      mockData.checkItemData.list = mockData.checkItemData.list.filter(item => item.id != itemId);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: `/admin/check-item/delete/${itemId}`,
      method: 'delete'
    })
  }
}

// 医院管理接口
export const hospitalApi = {
  // 获取医院列表
  getList(params) {
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.hospitalData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/hospital/list',
      method: 'get',
      params
    })
  },
  // 添加医院
  add(data) {
    if (useMockData) {
      const newHospital = {
        ...data,
        id: mockData.hospitalData.list.length + 1,
        appointmentCount: 0
      };
      mockData.hospitalData.list.unshift(newHospital);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/hospital/add',
      method: 'post',
      data
    })
  },
  // 更新医院
  update(data) {
    if (useMockData) {
      const index = mockData.hospitalData.list.findIndex(item => item.id === data.id);
      if (index !== -1) {
        mockData.hospitalData.list[index] = {
          ...mockData.hospitalData.list[index],
          ...data
        };
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/hospital/update',
      method: 'put',
      data
    })
  },
  // 获取医院详情
  getDetail(hospitalId) {
    if (useMockData) {
      const hospital = mockData.hospitalData.list.find(item => item.id == hospitalId);
      return mockData.mockResponse(hospital || {});
    }
    return request({
      url: `/admin/hospital/detail/${hospitalId}`,
      method: 'get'
    })
  },
  // 更新医院状态
  updateStatus(hospitalId, status) {
    if (useMockData) {
      const index = mockData.hospitalData.list.findIndex(item => item.id == hospitalId);
      if (index !== -1) {
        mockData.hospitalData.list[index].status = status;
      }
      return mockData.mockResponse({ success: true });
    }
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
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.appointmentData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/appointment/list',
      method: 'get',
      params
    })
  },
  // 获取预约详情
  getDetail(appointmentId) {
    if (useMockData) {
      const appointment = mockData.appointmentData.list.find(item => item.id == appointmentId);
      return mockData.mockResponse(appointment || {});
    }
    return request({
      url: `/admin/appointment/detail/${appointmentId}`,
      method: 'get'
    })
  },
  // 更新预约状态
  updateStatus(data) {
    if (useMockData) {
      const index = mockData.appointmentData.list.findIndex(item => item.id == data.id);
      if (index !== -1) {
        mockData.appointmentData.list[index].status = data.status;
      }
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/appointment/status',
      method: 'put',
      data
    })
  }
}

// 财务管理接口
export const financeApi = {
  // 获取财务统计
  getStatistics(params) {
    if (useMockData) {
      return mockData.mockResponse(mockData.financeData.stats);
    }
    return request({
      url: '/admin/finance/statistics',
      method: 'get',
      params
    })
  },
  // 获取财务列表
  getList(params) {
    if (useMockData) {
      const { pageNum = 1, pageSize = 10 } = params || {};
      const list = mockData.financeData.list;
      const total = list.length;
      const pageList = list.slice((pageNum - 1) * pageSize, pageNum * pageSize);
      
      return mockData.mockResponse({
        list: pageList,
        total
      });
    }
    return request({
      url: '/admin/finance/list',
      method: 'get',
      params
    })
  }
}

// 系统设置接口
export const settingApi = {
  // 获取系统设置
  getSettings() {
    if (useMockData) {
      return mockData.mockResponse(mockData.settingsData);
    }
    return request({
      url: '/admin/settings',
      method: 'get'
    })
  },
  // 更新系统设置
  updateSettings(data) {
    if (useMockData) {
      Object.assign(mockData.settingsData, data);
      return mockData.mockResponse({ success: true });
    }
    return request({
      url: '/admin/settings',
      method: 'put',
      data
    })
  }
} 